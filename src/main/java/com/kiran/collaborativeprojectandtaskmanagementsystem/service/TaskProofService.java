package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskProofMapper;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskProofResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.InsufficientPrivilegesException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ResourceNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.TaskProofRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TaskProofService {

    private final TaskProofRepo taskProofRepo;
    private final ProjectRepo projectRepo;
    private final ProjectMemberRepo projectMemberRepo;
    private final TaskRepo taskRepo;
    private final Cloudinary cloudinary;
    private final TaskProofMapper taskProofMapper;

    public TaskProofService(TaskProofRepo taskProofRepo, ProjectRepo projectRepo, ProjectMemberRepo projectMemberRepo, TaskRepo taskRepo, Cloudinary cloudinary, TaskProofMapper taskProofMapper) {
        this.taskProofRepo = taskProofRepo;
        this.projectRepo = projectRepo;
        this.projectMemberRepo = projectMemberRepo;
        this.taskRepo = taskRepo;
        this.cloudinary = cloudinary;
        this.taskProofMapper = taskProofMapper;
    }

    public void uploadProofs(Long projectId, Long taskId, MultipartFile file, Users user) throws IOException {

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

        if(!task.getAssignedUser().getId().equals(user.getId())){
            throw new InsufficientPrivilegesException("Task is not assigned to you");
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        TaskProof taskProof = new TaskProof();

        taskProof.setTask(task);
        taskProof.setUploadedBy(user);
        taskProof.setImageUrl(uploadResult.get("secure_url").toString());
        taskProof.setPublicId(uploadResult.get("public_id").toString());
        taskProof.setUploadedAt(LocalDateTime.now());

        taskProofRepo.save(taskProof);
    }

    public List<TaskProofResponseDTO> getTaskProofs(Long projectId, Long taskId, Users user) {

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

        List<TaskProof> taskProofs = taskProofRepo.findByTaskId(taskId);

        return taskProofs.stream()
                .map(taskProofMapper::toDTO)
                .toList();
    }

    public void deleteProof(Long projectId, Long taskId, Long proofId, Users user) throws IOException {

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

        if(!member.getId().equals(user.getId()) && member.getRole() != ProjectRole.OWNER && member.getRole() != ProjectRole.ADMIN){
            throw new InsufficientPrivilegesException("You dont have privileges");
        }

        TaskProof proof = taskProofRepo.findById(proofId).orElseThrow(
                () -> new ResourceNotFoundException("Proof not found with id: " + proofId)
        );

        cloudinary.uploader().destroy(proof.getPublicId(), ObjectUtils.emptyMap());

        taskProofRepo.delete(proof);
    }
}
