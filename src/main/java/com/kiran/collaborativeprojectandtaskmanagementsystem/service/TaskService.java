package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.AssignUserRequest;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateTaskRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskMapper;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.UserNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.TaskRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final ProjectRepo projectRepo;
    private final TaskMapper taskMapper;
    private final UserRepo userRepo;

    public void createTask(CreateTaskRequestDTO taskRequestDTO, Long projectId, Users user) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE){
            throw new RuntimeException("You are not a member in this project");
        }

        Task task = taskMapper.toEntity(taskRequestDTO, project, user);

        Long assignedUserId = taskRequestDTO.getAssignedUserId();
        if(assignedUserId != null){
            Users assignedUser = userRepo.findById(assignedUserId)
                    .orElseThrow(() -> new UserNotFoundException("User with id : " + assignedUserId + " not found"));

            ProjectMember assignedMember = projectMemberRepo.findByProjectAndUser(project, assignedUser);
            if(assignedMember == null || assignedMember.getStatus() != InvitationStatus.ACTIVE){
                throw new RuntimeException("Assigned user is not a member in this project");
            }

            task.setAssignedUser(assignedUser);
        }

        taskRepo.save(task);
    }

    public List<TaskResponseDTO> getAllTasks(Users user, Long projectId) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE){
            throw new RuntimeException("You are not a member in this project");
        }

        return taskRepo.getAllTasks(projectId);

    }

    @Transactional
    public void assignUserToTask(Long projectId, Long taskId, AssignUserRequest assignUserRequest, Users user) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id: " + taskId + " not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new RuntimeException("Task does not belong to this project");
        }

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new RuntimeException("You are not a member in this project");
        }
        if(member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN && task.getCreatedBy() != user){
            throw new RuntimeException("Insufficient privileges");
        }

        Long assignUserId = assignUserRequest.getAssignUserId();

        Users assignedUser = userRepo.findById(assignUserId)
                .orElseThrow(() -> new UserNotFoundException("User with id : " + assignUserId + " not found"));

        ProjectMember assignedMember = projectMemberRepo.findByProjectAndUser(project, assignedUser);
        if(assignedMember == null || assignedMember.getStatus() != InvitationStatus.ACTIVE){
            throw new RuntimeException("Assign user is not a member in this project");
        }

        task.setAssignedUser(assignedUser);

    }

    @Transactional
    public void updateTaskStatus(Long projectId, Long taskId, TaskStatus status, Users user){

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id: " + taskId + " not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new RuntimeException("Task does not belong to this project");
        }

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new RuntimeException("You are not a member in this project");
        }
        if(member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN && task.getCreatedBy() != user){
            throw new RuntimeException("Insufficient privileges");
        }

        task.setStatus(status);

    }
}
