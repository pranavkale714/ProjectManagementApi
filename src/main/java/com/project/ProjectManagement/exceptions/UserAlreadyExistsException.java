package com.project.ProjectManagement.exceptions;

public class UserAlreadyExistsException  extends  RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}