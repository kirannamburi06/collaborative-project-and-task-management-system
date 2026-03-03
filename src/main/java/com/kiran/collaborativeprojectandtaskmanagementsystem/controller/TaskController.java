package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateTaskRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequestDTO taskRequestDTO,
                                        @PathVariable Long projectId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        taskService.createTask(taskRequestDTO, projectId, user);

        return ResponseEntity.status(200).body("Task created successfully!");

    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<?> getAllTasks(@PathVariable Long projectId,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        Users user = userPrincipal.getUser();

        List<TaskResponseDTO> tasks = taskService.getAllTasks(user, projectId);

        return ResponseEntity.status(200).body(tasks);
    }

}
