package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

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
            Users assignedUser = userRepo.findById(taskRequestDTO.getAssignedUserId())
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
}
