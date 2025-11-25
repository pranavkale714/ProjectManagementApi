package com.project.ProjectManagement.dto;

import com.project.ProjectManagement.model.Project;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectCreationRequestDto {

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Project description is required")
    private String projectDescription;

    public Project toEntity() {
        return Project.builder()
                .projectName(this.projectName)
                .projectDescription(this.projectDescription)
                .build();
    }
}
