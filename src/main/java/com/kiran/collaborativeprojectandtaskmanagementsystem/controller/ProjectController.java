package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateProjectRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.InviteUserRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMemberResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequestDTO project,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        projectService.createProject(project, user);
        return ResponseEntity.status(200).body("Project Created");
    }

    @GetMapping
    public ResponseEntity<?> getProjects(@AuthenticationPrincipal UserPrincipal userPrincipal){
        Users user = userPrincipal.getUser();

        Map<Long, ProjectResponseDTO> projectList = projectService.getProjects(user);

        return ResponseEntity.status(200).body(projectList);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<?> inviteUserToProject(@RequestBody InviteUserRequestDTO request,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable Long id){
        Users user = userPrincipal.getUser();
        projectService.inviteUserToProject(request, user, id);

        return ResponseEntity.status(200).body("Invite request sent");
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptInvite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long id){
        Users user = userPrincipal.getUser();
        projectService.acceptInvite(user, id);

        return ResponseEntity.status(200).body("Invitation accepted");
    }

    @GetMapping("/invitations")
    public ResponseEntity<?> getInvitations(@AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        List<ProjectMemberResponseDTO> invitations = projectService.getAllInvitations(user);

        return ResponseEntity.status(200).body(invitations);

    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectInvite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long id){
        Users user = userPrincipal.getUser();
        projectService.rejectInvite(user, id);

        return ResponseEntity.status(200).body("Rejected invitation");
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();

        projectService.deleteProject(projectId, user);

        return ResponseEntity.status(200).body("Project deleted successfully");
    }
}