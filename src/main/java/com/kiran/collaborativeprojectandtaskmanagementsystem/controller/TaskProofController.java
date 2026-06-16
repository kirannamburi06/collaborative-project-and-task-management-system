package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ApiResponse;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskProofResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.TaskProofService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class TaskProofController {

    private final TaskProofService taskProofService;

    public TaskProofController(TaskProofService taskProofService) {
        this.taskProofService = taskProofService;
    }

    @PostMapping(
            value = "/{projectId}/tasks/{taskId}/proofs",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<?>> uploadProofs(@PathVariable Long projectId,
                                                       @PathVariable Long taskId,
                                                       @RequestParam MultipartFile file,
                                                       @AuthenticationPrincipal UserPrincipal userPrincipal
                                                       ) throws IOException {
        Users user = userPrincipal.getUser();
        taskProofService.uploadProofs(projectId, taskId, file, user);

        return ResponseEntity.status(200).body(
            new ApiResponse<>(
                    true,
                    "Proofs uploaded successfully",
                    null
            )
        );
    }

    @GetMapping("/{projectId}/tasks/{taskId}/proofs")
    public ResponseEntity<ApiResponse<List<TaskProofResponseDTO>>> getTaskProofs(@PathVariable Long projectId,
                                                     @PathVariable Long taskId,
                                                     @AuthenticationPrincipal UserPrincipal userPrincipal
                                                    ){
        Users user = userPrincipal.getUser();
        List<TaskProofResponseDTO> response = taskProofService.getTaskProofs(projectId, taskId, user);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(
                        true,
                        "Fetch proofs successful",
                        response
                )
        );
    }

    @DeleteMapping("/{projectId}/tasks/{taskId}/proofs/{proofId}")
    public ResponseEntity<ApiResponse<?>> deleteProof(@PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @PathVariable Long proofId,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal
                                                    ) throws IOException {
        Users user = userPrincipal.getUser();

        taskProofService.deleteProof(projectId, taskId, proofId, user);

        return ResponseEntity.status(200).body(
                new ApiResponse<>(
                        true,
                        "Proof deleted successfully",
                        null
                )
        );
    }

}
