package com.project.ProjectManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {

    @NotBlank(message = "User email is required")
    @Email(message = "Please provide a valid email")
    private String userEmail;

    @NotBlank(message = "User password is required")
    private String userPassword;
}
