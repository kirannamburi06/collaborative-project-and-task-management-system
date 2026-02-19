package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.CreateProjectRequestDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMapper;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final ProjectMemberRepo projectMemberRepo;

    @Transactional
    public void createProject(CreateProjectRequestDTO requestDTO, Users user){

        Project project = projectMapper.toEntity(requestDTO, user);
        projectRepo.save(project);

        ProjectMember owner = new ProjectMember();
        owner.setUser(user);
        owner.setProject(project);
        owner.setRole(ProjectRole.OWNER);
        owner.setStatus(InvitationStatus.ACTIVE);
        owner.setJoinedAt(LocalDateTime.now());
        projectMemberRepo.save(owner);

    }

    public List<ProjectResponseDTO> getProjects(Users user) {

        return projectRepo.getAllProjectsWithMembers(user.getUsername()).stream()
                .map(projectMapper::toDTO)
                .toList();
    }
}
