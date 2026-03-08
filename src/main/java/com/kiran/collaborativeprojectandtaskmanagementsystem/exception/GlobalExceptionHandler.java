package com.kiran.collaborativeprojectandtaskmanagementsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiError> handleProjectNotFoundException(ProjectNotFoundException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(UserIsAlreadyAMemberException.class)
    public ResponseEntity<ApiError> handeUserIsAlreadyAMemberException(UserIsAlreadyAMemberException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(InsufficientPrivilegesException.class)
    public ResponseEntity<ApiError> handleInsufficientPrivilegesException(InsufficientPrivilegesException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(403).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e){
        log.error(e.getMessage());

        ApiError error = new ApiError(e.getMessage());

        return ResponseEntity.status(404).body(error);
    }
}
