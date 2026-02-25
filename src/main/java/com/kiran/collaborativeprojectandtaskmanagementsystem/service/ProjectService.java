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
import java.util.List;

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

    public List<ProjectResponseDTO> getProjects(Users user) {

        return projectRepo.getAllProjectsWithMembers(user.getUsername()).stream()
                .map(projectMapper::toDTO)
                .toList();
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

        if(projectMemberRepo.existsByProjectAndUser(project, invitedUser)){
            throw new RuntimeException("user is already a member in this project");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(invitedUser);
        member.setRole(request.getRole());
        member.setStatus(InvitationStatus.INVITED);
        member.setInvitedBy(user);

        projectMemberRepo.save(member);

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

        List<ProjectMember> invitations = projectMemberRepo.findByUserAndStatus(user, InvitationStatus.INVITED);

        if(invitations == null){
            throw new RuntimeException("No invitations found");
        }

        return invitations.stream()
                .map(projectMemberMapper::toDTO)
                .toList();
    }
}
