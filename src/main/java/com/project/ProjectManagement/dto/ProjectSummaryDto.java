package com.project.ProjectManagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProjectSummaryDto {

    private UUID projectId;
    private String projectName;
    private String projectDescription;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
