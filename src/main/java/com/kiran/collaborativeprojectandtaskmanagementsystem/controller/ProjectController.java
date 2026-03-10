package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.InvitationStatus;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectRole;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(@RequestBody CreateProjectRequestDTO project,
                                                              @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        ProjectResponseDTO projectResponse = projectService.createProject(project, user);

        ApiResponse<ProjectResponseDTO> response = new ApiResponse<>(
                true,
                "Project created",
                projectResponse
        );

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<ProjectResponseDTO>>> getProjects(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestParam(required = false) InvitationStatus status,
                                         @RequestParam(required = false) ProjectRole role,
                                         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                         Pageable pageable){
        Users user = userPrincipal.getUser();

        PageResponseDTO<ProjectResponseDTO> projectList = projectService.getProjects(
                user,
                status,
                role,
                pageable);

        ApiResponse<PageResponseDTO<ProjectResponseDTO>> response = new ApiResponse<>(
                true,
                "Fetch all projects success",
                projectList
        );

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> inviteUserToProject(@RequestBody InviteUserRequestDTO request,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable Long id){
        Users user = userPrincipal.getUser();
        ProjectResponseDTO projectResponse = projectService.inviteUserToProject(request, user, id);

        ApiResponse<ProjectResponseDTO> response = new ApiResponse<ProjectResponseDTO>(
                true,
                "User invited successfully",
                null
        );

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> acceptInvite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long id){
        Users user = userPrincipal.getUser();
        ProjectResponseDTO projectResponse = projectService.acceptInvite(user, id);

        ApiResponse<ProjectResponseDTO> response = new ApiResponse<ProjectResponseDTO>(
                true,
                "Invitation accepted successfully",
                null
        );

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/invitations")
    public ResponseEntity<ApiResponse<List<ProjectMemberResponseDTO>>> getInvitations(@AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        List<ProjectMemberResponseDTO> invitations = projectService.getAllInvitations(user);

        ApiResponse<List<ProjectMemberResponseDTO>> response = new ApiResponse<>(
                true,
                "Fetch all invitations successful",
                invitations
        );

        return ResponseEntity.status(200).body(response);

    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> rejectInvite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long id){
        Users user = userPrincipal.getUser();
        ProjectResponseDTO projectResponse = projectService.rejectInvite(user, id);

        ApiResponse<ProjectResponseDTO> response = new ApiResponse<>(
                true,
                "Rejected invitation successfully",
                projectResponse
        );

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> deleteProject(@PathVariable Long projectId,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();

        ProjectResponseDTO projectResponse = projectService.deleteProject(projectId, user);

        ApiResponse<ProjectResponseDTO> response = new ApiResponse<>(
                true,
                "Project deleted successfully",
                projectResponse
        );

        return ResponseEntity.status(200).body(response);
    }
}