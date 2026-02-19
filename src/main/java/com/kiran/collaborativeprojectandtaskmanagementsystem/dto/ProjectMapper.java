package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProjectMapper {

    public Project toEntity(CreateProjectRequestDTO requestDTO,
                            Users user){
        Project project = new Project();

        project.setName(requestDTO.getName());
        project.setDescription(requestDTO.getDescription());
        project.setCreatedBy(user);
        project.setCreatedAt(LocalDateTime.now());

        return project;
    }

    public ProjectResponseDTO toDTO(Project project){
        ProjectResponseDTO dto = new ProjectResponseDTO();

        dto.setId(project.getId());
        dto.setDescription(project.getDescription());
        dto.setName(project.getName());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setCreatedBy(project.getCreatedBy().getUsername());
        dto.setMembers(project.getMembers().stream()
                .map(p -> p.getUser().getUsername())
                .toList());

        return dto;
    }

}
