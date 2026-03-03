package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepo projectMemberRepo;
    private final UserRepo userRepo;
    private final ProjectMemberMapper projectMemberMapper;

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

    }

    public Map<Long, ProjectResponseDTO> getProjects(Users user) {

        List<ProjectFlatDTO> projects = projectMemberRepo.getProjectsWithMembers(user);

        Map<Long, ProjectResponseDTO> grouped = new LinkedHashMap<>();
        for(ProjectFlatDTO flatDTO : projects) {
            Long projectId = flatDTO.getId();
            if(!grouped.containsKey(projectId)){
                grouped.put(projectId, new ProjectResponseDTO(
                        flatDTO.getId(),
                        flatDTO.getName(),
                        flatDTO.getDescription(),
                        flatDTO.getCreatedAt(),
                        flatDTO.getCreatedBy(),
                        new ArrayList<>()
                ));
            }
            grouped.get(projectId).getMembers().add(flatDTO.getUsername());
        }

        return grouped;
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
                throw new RuntimeException("user is already a member in this project");
            } else if (pm.getStatus() == InvitationStatus.INVITED) {
                pm.setRole(request.getRole());
                projectMemberRepo.save(pm);
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
    }

    private void validateRole(Project project, Users user, ProjectRole... roles) {

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null){
            throw new RuntimeException("You are Not a member in this project, so you cannot invite users");
        }

        for(ProjectRole role : roles){
            if(role == member.getRole())
                return;
        }

        throw new RuntimeException("You have no privileges to add new members into this project");
    }

    @Transactional
    public void acceptInvite(Users user, Long id) {

        Project project = projectRepo.findById(id).orElseThrow(
                () -> new ProjectNotFoundException("Project with id : " + id + " not found")
        );

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);

        if(member == null){
            throw new RuntimeException("Invitation not found");
        }


        if(member.getStatus() == InvitationStatus.ACTIVE){
            throw new RuntimeException("You are already a active member!");
        }
        else if(member.getStatus() != InvitationStatus.INVITED){
            throw new RuntimeException("You are not invited, so you cannot accept or join!");
        }

        member.setStatus(InvitationStatus.ACTIVE);
        member.setJoinedAt(LocalDateTime.now());
    }

    public List<ProjectMemberResponseDTO> getAllInvitations(Users user){

        List<ProjectMemberResponseDTO> invitations =  projectMemberRepo.findAllInvitationsDTO(user, InvitationStatus.INVITED);

        if(invitations == null){
            throw new RuntimeException("No invitations");
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
            throw new RuntimeException("Invitation not found");
        }

        if(member.getStatus() == InvitationStatus.ACTIVE || member.getStatus() == InvitationStatus.REJECTED){
            throw new RuntimeException("Failed to reject. You are either an active member or you already rejected");
        }

        member.setStatus(InvitationStatus.REJECTED);

    }
}
