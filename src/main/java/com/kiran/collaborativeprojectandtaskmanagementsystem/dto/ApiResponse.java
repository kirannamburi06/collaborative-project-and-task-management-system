package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
    private final LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
        timestamp = LocalDateTime.now();
    }
}
