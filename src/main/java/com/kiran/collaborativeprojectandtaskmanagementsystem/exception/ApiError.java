package com.kiran.collaborativeprojectandtaskmanagementsystem.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private final boolean success;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiError(String message){
        success = false;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
