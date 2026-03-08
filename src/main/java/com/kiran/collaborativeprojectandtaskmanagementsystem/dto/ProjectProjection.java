package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface ProjectProjection {

    Long getId();
    String getName();
    String getDescription();
    LocalDateTime getCreatedAt();
    String getCreatedBy();

}
