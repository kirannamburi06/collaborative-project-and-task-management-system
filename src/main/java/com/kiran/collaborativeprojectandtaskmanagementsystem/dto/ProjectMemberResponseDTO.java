package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberResponseDTO {

    private Long projectId;
    private String projectName;
    private String invitedBy;
    private String role;

}
