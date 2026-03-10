package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.InsufficientPrivilegesException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ResourceNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.UserNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.TaskRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final ProjectRepo projectRepo;
    private final TaskMapper taskMapper;
    private final UserRepo userRepo;
    private final ActivityLogService activityLogService;

    @Transactional
    public TaskResponseDTO createTask(CreateTaskRequestDTO taskRequestDTO, Long projectId, Users user) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE){
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }

        Task task = taskMapper.toEntity(taskRequestDTO, project, user);

        Long assignedUserId = taskRequestDTO.getAssignedUserId();
        if(assignedUserId != null){
            Users assignedUser = userRepo.findById(assignedUserId)
                    .orElseThrow(() -> new UserNotFoundException("User with id : " + assignedUserId + " not found"));

            ProjectMember assignedMember = projectMemberRepo.findByProjectAndUser(project, assignedUser);
            if(assignedMember == null || assignedMember.getStatus() != InvitationStatus.ACTIVE){
                throw new InsufficientPrivilegesException("Assigned user is not a member in this project");
            }

            task.setAssignedUser(assignedUser);
        }

        taskRepo.save(task);

        activityLogService.log(
                ActivityType.TASK_CREATED,
                user,
                project,
                EntityType.TASK,
                task.getId()
                );

        return taskMapper.toDTO(task);
    }

    public PageResponseDTO<TaskResponseDTO> getAllTasks(Users user,
                                                        Long projectId,
                                                        TaskStatus status,
                                                        Long assignedTo,
                                                        Pageable pageable) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE){
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }

        Page<TaskResponseDTO> response = taskRepo.getAllTasks(
                projectId,
                status,
                assignedTo,
                pageable);

        return new PageResponseDTO<TaskResponseDTO>(
                response.getContent(),
                response.getNumber(),
                response.getSize(),
                response.getTotalElements(),
                response.getTotalPages()
        );
    }

    @Transactional
    public TaskResponseDTO assignUserToTask(Long projectId, Long taskId, AssignUserRequest assignUserRequest, Users user) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task does not belong to this project");
        }

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }
        if(member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN && task.getCreatedBy() != user){
            throw new InsufficientPrivilegesException("Insufficient privileges");
        }

        Long assignUserId = assignUserRequest.getAssignUserId();

        Users assignedUser = userRepo.findById(assignUserId)
                .orElseThrow(() -> new UserNotFoundException("User with id : " + assignUserId + " not found"));

        ProjectMember assignedMember = projectMemberRepo.findByProjectAndUser(project, assignedUser);
        if(assignedMember == null || assignedMember.getStatus() != InvitationStatus.ACTIVE){
            throw new InsufficientPrivilegesException("Assign user is not a member in this project");
        }

        task.setAssignedUser(assignedUser);

        activityLogService.log(
                ActivityType.TASK_ASSIGNED,
                user,
                project,
                EntityType.TASK,
                task.getId()
        );

        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskResponseDTO updateTaskStatus(Long projectId, Long taskId, TaskStatus status, Users user){

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task does not belong to this project");
        }

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }
        if(member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN && task.getCreatedBy() != user){
            throw new InsufficientPrivilegesException("Insufficient privileges");
        }

        task.setStatus(status);

        activityLogService.log(
                ActivityType.TASK_STATUS_UPDATED,
                user,
                project,
                EntityType.TASK,
                task.getId()
        );

        return taskMapper.toDTO(task);
    }

    @Transactional
    public TaskResponseDTO deleteTask(Long projectId, Long taskId, Users user) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + projectId + " not found"));

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found"));

        if (!task.getProject().getId().equals(projectId)) {
            throw new ResourceNotFoundException("Task does not belong to this project");
        }

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }
        if(member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN && task.getCreatedBy() != user){
            throw new InsufficientPrivilegesException("Insufficient privileges");
        }

        taskRepo.delete(task);

        activityLogService.log(
                ActivityType.TASK_DELETED,
                user,
                project,
                EntityType.TASK,
                task.getId()
        );

        return taskMapper.toDTO(task);
    }
}
