package com.project.ProjectManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationErrorResponse {

    private String errorMessage;
    private int errorCode;
    private LocalDateTime occurredAt;

}
