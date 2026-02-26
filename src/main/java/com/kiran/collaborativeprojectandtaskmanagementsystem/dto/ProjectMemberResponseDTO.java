package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberResponseDTO {

    private Long projectId;
    private String projectName;
    private String invitedBy;
    private ProjectRole role;

}
