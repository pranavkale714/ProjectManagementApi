package com.project.ProjectManagement.service;

import com.project.ProjectManagement.dto.ProjectCreationRequestDto;
import com.project.ProjectManagement.dto.ProjectSummaryDto;
import com.project.ProjectManagement.exceptions.EntityNotFoundException;
import com.project.ProjectManagement.exceptions.UnauthorizedAccessException;
import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.ProjectRepo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepo projectRepo;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public Project createProject(ProjectCreationRequestDto requestDto, User loggedInUser) {

        logger.info("Creating project for user: {} ({})", loggedInUser.getFullName(), loggedInUser.getEmailAddress());

        Project newProject = requestDto.toEntity();
        newProject.setOwner(loggedInUser);
        Project savedProject = projectRepo.save(newProject);

        logger.debug("Project entity before save: {}", newProject);
        logger.info("Project '{}' created successfully with ID: {}", savedProject.getProjectName(), savedProject.getProjectId());

        return savedProject;
    }

    public List<ProjectSummaryDto> fetchAllProjects() {
        logger.info("Fetching all projects from database...");
        List<Project> projects = projectRepo.findAll();

        if (projects.isEmpty()) {
            logger.warn("No projects found in the database!");
        } else {
            logger.info("Fetched {} projects from the database.", projects.size());
        }

        return projects.stream()
                .map(project -> ProjectSummaryDto.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .projectDescription(project.getProjectDescription())
                        .createdOn(project.getCreatedOn())
                        .updatedOn(project.getUpdatedOn())
                        .build())
                .toList();
    }

    public Project updateProject(UUID projectId, ProjectCreationRequestDto requestDto, User loggedInUser) {
        Project existingProject = projectRepo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        existingProject.setProjectName(requestDto.getProjectName());
        existingProject.setProjectDescription(requestDto.getProjectDescription());
        existingProject.setOwner(loggedInUser);
        existingProject.setUpdatedOn(LocalDateTime.now());

        return projectRepo.save(existingProject);
    }

    public void removeProject(UUID projectId, User loggedInUser) {
        Project existingProject = projectRepo.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        if (!existingProject.getOwner().getUserId().equals(loggedInUser.getUserId())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this project");
        }

        logger.info("Deleting project with ID: {}", projectId);
        projectRepo.delete(existingProject);
        logger.info("Project deleted successfully with ID: {}", projectId);
    }
}