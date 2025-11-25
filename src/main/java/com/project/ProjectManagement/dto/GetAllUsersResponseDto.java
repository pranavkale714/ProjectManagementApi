package com.project.ProjectManagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class GetAllUsersResponseDto {

        private UUID id;
        private String username;
        private String email;

}


