package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectMember;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {

    public ProjectMemberResponseDTO toDTO(ProjectMember member){
        ProjectMemberResponseDTO dto = new ProjectMemberResponseDTO();
        dto.setProjectId(member.getProject().getId());
        dto.setProjectName(member.getProject().getName());
        //dto.setRole(String.valueOf(member.getRole()));
        dto.setInvitedBy(member.getInvitedBy().getUsername());

        return dto;
    }

}
