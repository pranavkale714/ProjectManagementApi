package com.project.ProjectManagement.dto;


import com.project.ProjectManagement.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Please provide a valid email")
    private String userEmail;

    @NotBlank(message = "Password is required")
    private String userPassword;

    public User toEntity() {
        return User.builder()
                .fullName(this.fullName)
                .emailAddress(this.userEmail)
                .passwordHash(this.userPassword)
                .build();
    }
}
