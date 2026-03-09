package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.InsufficientPrivilegesException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ResourceNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.UserIsAlreadyAMemberException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepo projectMemberRepo;
    private final UserRepo userRepo;
    private final ProjectMemberMapper projectMemberMapper;
    private final ActivityLogService activityLogService;

    @Transactional
    public void createProject(CreateProjectRequestDTO requestDTO, Users user){

        Project project = projectMapper.toEntity(requestDTO, user);
        projectRepo.save(project);

        ProjectMember owner = new ProjectMember();
        owner.setUser(user);
        owner.setProject(project);
        owner.setRole(ProjectRole.OWNER);
        owner.setStatus(InvitationStatus.ACTIVE);
        owner.setJoinedAt(LocalDateTime.now());
        projectMemberRepo.save(owner);

        activityLogService.log(
                ActivityType.PROJECT_CREATED,
                user,
                project,
                EntityType.PROJECT,
                project.getId()
        );
    }

    public PageResponseDTO<ProjectResponseDTO> getProjects(Users user,
                                                           InvitationStatus status,
                                                           ProjectRole role,
                                                           Pageable pageable) {

        if(status == null){
            status = InvitationStatus.ACTIVE;
        }

        Page<ProjectProjection> projectPage = projectMemberRepo.findProjectsForUser(
                user,
                status,
                role,
                pageable);

        List<Long> projectIds = projectPage.getContent().stream()
                .map(ProjectProjection::getId)
                .toList();

        List<ProjectMemberProjection> projectMembers = projectMemberRepo.findMembersByProjectIds(projectIds);

        Map<Long, List<String>> membersMap = projectMembers.stream()
                .collect(Collectors.groupingBy(
                        ProjectMemberProjection::getProjectId,
                        Collectors.mapping(
                                ProjectMemberProjection::getUsername,
                                Collectors.toList())
                ));

        List<ProjectResponseDTO> response = projectPage.getContent().stream()
                .map(p -> new ProjectResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getCreatedAt(),
                        p.getCreatedBy(),
                        membersMap.getOrDefault(p.getId(), new ArrayList<>())
                ))
                .toList();

        return new PageResponseDTO<ProjectResponseDTO>(
                response,
                projectPage.getNumber(),
                projectPage.getSize(),
                projectPage.getTotalElements(),
                projectPage.getTotalPages()
        );
    }

    @Transactional
    public void inviteUserToProject(InviteUserRequestDTO request, Users user, Long id) {

        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project with id : " + id + " not found"
        ));

        validateRole(project, user, ProjectRole.OWNER, ProjectRole.ADMIN);

        Users invitedUser = userRepo.findUsersByUsername(request.getUsername());

        if(invitedUser == null){
            throw new UsernameNotFoundException("User not found");
        }

        ProjectMember pm = projectMemberRepo.findByProjectAndUser(project, invitedUser);
        if(pm != null){
            if(pm.getStatus() == InvitationStatus.ACTIVE) {
                throw new UserIsAlreadyAMemberException("user is already a member in this project");
            } else if (pm.getStatus() == InvitationStatus.INVITED) {
                pm.setRole(request.getRole());
                projectMemberRepo.save(pm);
            } else if (pm.getStatus() == InvitationStatus.REJECTED){
                pm.setRole(request.getRole());
                pm.setStatus(InvitationStatus.INVITED);
            }
        }else {

            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setUser(invitedUser);
            member.setRole(request.getRole());
            member.setStatus(InvitationStatus.INVITED);
            member.setInvitedBy(user);

            projectMemberRepo.save(member);
        }

        activityLogService.log(
                ActivityType.USER_INVITED,
                user,
                project,
                EntityType.PROJECT,
                project.getId()
        );
    }

    private void validateRole(Project project, Users user, ProjectRole... roles) {

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null){
            throw new InsufficientPrivilegesException("You are Not a member in this project, so you cannot invite users");
        }

        for(ProjectRole role : roles){
            if(role == member.getRole())
                return;
        }

        throw new InsufficientPrivilegesException("You have no privileges to add new members into this project");
    }

    @Transactional
    public void acceptInvite(Users user, Long id) {

        Project project = projectRepo.findById(id).orElseThrow(
                () -> new ProjectNotFoundException("Project with id : " + id + " not found")
        );

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null){
            throw new ResourceNotFoundException("Invitation not found");
        }


        if(member.getStatus() == InvitationStatus.ACTIVE){
            throw new UserIsAlreadyAMemberException("You are already a active member!");
        }
        else if(member.getStatus() != InvitationStatus.INVITED){
            throw new InsufficientPrivilegesException("You are not invited, so you cannot accept or join!");
        }

        member.setStatus(InvitationStatus.ACTIVE);
        member.setJoinedAt(LocalDateTime.now());

        activityLogService.log(
                ActivityType.USER_ACCEPTED,
                user,
                project,
                EntityType.PROJECT,
                project.getId()
        );
    }

    public List<ProjectMemberResponseDTO> getAllInvitations(Users user){

        List<ProjectMemberResponseDTO> invitations =  projectMemberRepo.findAllInvitationsDTO(user, InvitationStatus.INVITED);

        if(invitations == null){
            throw new ResourceNotFoundException("No invitations");
        }

        return invitations;
    }

    @Transactional
    public void rejectInvite(Users user, Long id) {

        Project project = projectRepo.findById(id).orElseThrow(
                () -> new ProjectNotFoundException("Project with id : " + id + " not found")
        );

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null){
            throw new ResourceNotFoundException("Invitation not found");
        }

        if(member.getStatus() == InvitationStatus.ACTIVE || member.getStatus() == InvitationStatus.REJECTED){
            throw new UserIsAlreadyAMemberException("Failed to reject. You are either an active member or you already rejected");
        }

        member.setStatus(InvitationStatus.REJECTED);

        activityLogService.log(
                ActivityType.USER_REJECTED,
                user,
                project,
                EntityType.PROJECT,
                project.getId()
        );
    }

    @Transactional
    public void deleteProject(Long projectId, Users user) {

        Project project = projectRepo.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id : " + projectId + " not found")
        );

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null || member.getStatus() != InvitationStatus.ACTIVE){
            throw new InsufficientPrivilegesException("You are not an active member in this project");
        }

        if(!project.getCreatedBy().getId().equals(user.getId())){
            throw new InsufficientPrivilegesException("Only owner can delete project");
        }

        projectRepo.delete(project);

        activityLogService.log(
                ActivityType.PROJECT_DELETED,
                user,
                project,
                EntityType.PROJECT,
                project.getId()
        );
    }
}
