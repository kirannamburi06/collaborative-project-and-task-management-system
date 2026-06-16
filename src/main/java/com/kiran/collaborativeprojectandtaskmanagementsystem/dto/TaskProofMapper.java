package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.TaskProof;
import org.springframework.stereotype.Component;

@Component
public class TaskProofMapper {

    public TaskProofResponseDTO toDTO(TaskProof taskProof){

        TaskProofResponseDTO response = new TaskProofResponseDTO();

        response.setId(taskProof.getId());
        response.setImageUrl(taskProof.getImageUrl());
        response.setUploadedAt(taskProof.getUploadedAt());
        response.setUploadedBy(taskProof.getUploadedBy().getUsername());

        return response;
    }

}
