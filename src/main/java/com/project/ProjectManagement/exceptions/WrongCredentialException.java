package com.project.ProjectManagement.exceptions;

public class WrongCredentialException extends RuntimeException{

    public WrongCredentialException(String message){
        super(message);
    }
}