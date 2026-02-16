package com.kiran.collaborativeprojectandtaskmanagementsystem.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message){
        super(message);
    }

}
