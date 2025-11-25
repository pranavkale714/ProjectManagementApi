package com.project.ProjectManagement.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProjectModificationDto {

    private UUID projectId;
    private String projectName;
    private String projectDescription;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
