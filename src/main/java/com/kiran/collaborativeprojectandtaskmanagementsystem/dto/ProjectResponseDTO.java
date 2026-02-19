package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String Description;
    private LocalDateTime createdAt;
    private String createdBy;
    private List<String> members;

}
