package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Task;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequestDTO taskRequestDTO,
                         Project project, Users user){
        Task task = new Task();

        task.setName(taskRequestDTO.getName());
        task.setDescription(taskRequestDTO.getDescription());
        task.setProject(project);
        task.setCreatedBy(user);

        return task;
    }

    public TaskResponseDTO toDTO(Task task){
        TaskResponseDTO dto = new TaskResponseDTO();

        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedBy(task.getCreatedBy().getUsername());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setAssignedUser(task.getAssignedUser() == null ? null : task.getAssignedUser().getUsername());
        dto.setUpdatedAt(task.getUpdatedAt());

        return dto;
    }

}
