package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.TaskStatus;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(@RequestBody CreateTaskRequestDTO taskRequestDTO,
                                        @PathVariable Long projectId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        TaskResponseDTO taskResponse = taskService.createTask(taskRequestDTO, projectId, user);

        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                true,
                "Task created successfully!",
                taskResponse
        );

        return ResponseEntity.status(200).body(response);

    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<ApiResponse<PageResponseDTO<TaskResponseDTO>>> getAllTasks(@PathVariable Long projectId,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestParam(required = false) TaskStatus status,
                                         @RequestParam(required = false) Long assignedTo,
                                         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                             Pageable pageable){
        Users user = userPrincipal.getUser();

        PageResponseDTO<TaskResponseDTO> tasks = taskService.getAllTasks(
                user,
                projectId,
                status,
                assignedTo,
                pageable
                );

        ApiResponse<PageResponseDTO<TaskResponseDTO>> response = new ApiResponse<>(
                true,
                "Fetch tasks successful",
                tasks
        );

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/{projectId}/tasks/{taskId}/assign")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> assignUserToTask(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestBody AssignUserRequest assignUserRequest,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal){
        Users user = userPrincipal.getUser();

        TaskResponseDTO taskResponse = taskService.assignUserToTask(projectId, taskId, assignUserRequest, user);

        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                true,
                "User successfully assigned to task",
                taskResponse
        );

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{projectId}/tasks/{taskId}/update")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTaskStatus(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestParam(required = false) TaskStatus status,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(status == null){
            status = TaskStatus.IN_PROGRESS;
        }

        Users user = userPrincipal.getUser();

        TaskResponseDTO taskResponse = taskService.updateTaskStatus(projectId, taskId, status, user);

        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                true,
                "Updated the status successfully",
                taskResponse
        );

        return ResponseEntity.status(200).body(response);

    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> deleteTask(@PathVariable Long projectId,
                                        @PathVariable Long taskId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();

        TaskResponseDTO taskResponse = taskService.deleteTask(projectId, taskId, user);

        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                true,
                "Deleted the task successfully",
                taskResponse
        );

        return ResponseEntity.status(200).body(response);

    }
}
