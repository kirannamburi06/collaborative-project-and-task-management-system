package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.AssignUserRequest;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateTaskRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.PageResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskResponseDTO;
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
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequestDTO taskRequestDTO,
                                        @PathVariable Long projectId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        taskService.createTask(taskRequestDTO, projectId, user);

        return ResponseEntity.status(200).body("Task created successfully!");

    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<?> getAllTasks(@PathVariable Long projectId,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                             Pageable pageable){
        Users user = userPrincipal.getUser();

        PageResponseDTO<TaskResponseDTO> tasks = taskService.getAllTasks(user, projectId, pageable);

        return ResponseEntity.status(200).body(tasks);
    }

    @PostMapping("/{projectId}/tasks/{taskId}/assign")
    public ResponseEntity<?> assignUserToTask(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestBody AssignUserRequest assignUserRequest,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal){
        Users user = userPrincipal.getUser();

        taskService.assignUserToTask(projectId, taskId, assignUserRequest, user);

        return ResponseEntity.status(200).body("User successfully assigned to task");
    }

    @PatchMapping("/{projectId}/tasks/{taskId}/update")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long projectId,
                                              @PathVariable Long taskId,
                                              @RequestParam(required = false) TaskStatus status,
                                              @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(status == null){
            status = TaskStatus.IN_PROGRESS;
        }

        Users user = userPrincipal.getUser();

        taskService.updateTaskStatus(projectId, taskId, status, user);

        return ResponseEntity.status(200).body("Updated the status successfully");

    }

    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long projectId,
                                        @PathVariable Long taskId,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();

        taskService.deleteTask(projectId, taskId, user);

        return ResponseEntity.status(200).body("Deleted the task successfully");

    }
}
