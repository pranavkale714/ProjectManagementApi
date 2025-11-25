package com.project.ProjectManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskDeletionResponseDto {

    private String infoMessage;
    private UUID idOfTask;
    private LocalDateTime deletedAt;

}
