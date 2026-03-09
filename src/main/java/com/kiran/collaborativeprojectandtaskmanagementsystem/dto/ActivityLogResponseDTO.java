package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ActivityType;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.EntityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class ActivityLogResponseDTO {

    private ActivityType activityType;
    private String performedBy;
    private Long projectId;
    private EntityType entityType;
    private Long entityId;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime createdAt;

}
