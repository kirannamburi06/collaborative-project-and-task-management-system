package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateProjectRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        List<ProjectResponseDTO> projectList = projectService.getProjects(user);

        return ResponseEntity.status(200).body(projectList);
    }

}