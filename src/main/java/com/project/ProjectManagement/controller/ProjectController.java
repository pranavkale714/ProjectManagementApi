package com.project.ProjectManagement.controller;

import com.project.ProjectManagement.dto.*;
import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import com.project.ProjectManagement.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepo userRepository;


    @PostMapping
    public ResponseEntity<ProjectResponseDto> createNewProject(@Valid @RequestBody ProjectCreationRequestDto projectDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmailAddress(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectService.createProject(projectDto, user);

        ProjectResponseDto response = new ProjectResponseDto(
                project.getProjectId(),
                project.getProjectName(),
                project.getProjectDescription(),
                project.getCreatedOn()
        );


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectSummaryDto>> fetchAllProjects() {
        List<ProjectSummaryDto> projects = projectService.fetchAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectModificationResponseDto> updateExistingProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectCreationRequestDto projectDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmailAddress(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project updatedProject = projectService.updateProject(id, projectDto, user);

        ProjectModificationResponseDto response = ProjectModificationResponseDto.builder()
                .projectId(updatedProject.getProjectId())
                .projectName(updatedProject.getProjectName())
                .projectDescription(updatedProject.getProjectDescription())
                .createdOn(updatedProject.getCreatedOn())
                .updatedOn(updatedProject.getUpdatedOn())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        User user = userRepository.findByEmailAddress(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        projectService.removeProject(id, user);

        return ResponseEntity.ok(Map.of(
                "message", "Project deleted successfully",
                "projectId", id
        ));
    }
}
