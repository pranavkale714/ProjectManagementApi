package com.project.ProjectManagement.service;


import com.project.ProjectManagement.dto.TaskDetailDto;
import com.project.ProjectManagement.enums.Priority;
import com.project.ProjectManagement.enums.Status;
import com.project.ProjectManagement.exceptions.EntityNotFoundException;
import com.project.ProjectManagement.exceptions.UnauthorizedAccessException;
import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.Task;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.ProjectRepo;
import com.project.ProjectManagement.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskDetailDto createTask(UUID projectId, com.example.projectmanagement.dto.TaskCreationRequestDto request, User loggedInUser) {
        logger.info("Attempting to create task '{}' under project ID: {}", request.getTaskTitle(), projectId);

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> {
                    logger.error("Project not found with ID: {}", projectId);
                    return new EntityNotFoundException("Project not found with ID: " + projectId);
                });

        if (!project.getOwner().getUserId().equals(loggedInUser.getUserId())) {
            logger.warn("User {} tried to create task in unauthorized project {}", loggedInUser.getEmailAddress(), projectId);
            throw new UnauthorizedAccessException("You are not authorized to add tasks to this project");
        }

        Task task = request.toEntity();
        task.setProject(project);
        task.setAssignedUser(loggedInUser);
        Task savedTask = taskRepo.save(task);

        logger.info("Task '{}' created successfully with ID: {}", task.getTaskTitle(), task.getTaskId());
        return mapToResponse(savedTask);
    }

    public TaskDetailDto updateTask(UUID taskId, com.example.projectmanagement.dto.TaskCreationRequestDto request, User loggedInUser) {
        logger.info("Updating task with ID: {}", taskId);

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        if (!task.getAssignedUser().getUserId().equals(loggedInUser.getUserId())) {
            throw new UnauthorizedAccessException("You are not authorized to update this task");
        }

        task.setTaskTitle(request.getTaskTitle());
        task.setTaskDescription(request.getTaskDescription());
        task.setTaskPriority(request.getTaskPriority());
        task.setTaskStatus(request.getTaskStatus());
        task.setTaskDueDate(request.getTaskDueDate());
        task.setUpdatedOn(LocalDateTime.now());

        Task updatedTask = taskRepo.save(task);
        logger.info("Task '{}' updated successfully with ID: {}", updatedTask.getTaskTitle(), updatedTask.getTaskId());
        return mapToResponse(updatedTask);
    }

    public List<TaskDetailDto> listTasks(User loggedInUser, Status status, Priority priority, String sortBy) {
        List<Task> tasks = taskRepo.findByAssignedUser(loggedInUser);

        if (status != null) tasks = tasks.stream().filter(t -> t.getTaskStatus() == status).toList();
        if (priority != null) tasks = tasks.stream().filter(t -> t.getTaskPriority() == priority).toList();

        if ("dueDate".equalsIgnoreCase(sortBy))
            tasks = tasks.stream().sorted(Comparator.comparing(Task::getTaskDueDate)).toList();
        else if ("priority".equalsIgnoreCase(sortBy))
            tasks = tasks.stream().sorted(Comparator.comparing(Task::getTaskPriority)).toList();

        return tasks.stream().map(this::mapToResponse).toList();
    }

    public List<TaskDetailDto> searchTasks(User loggedInUser, String keyword) {
        logger.info("Fetching all tasks for user: {}", loggedInUser.getEmailAddress());
        return taskRepo.searchUserTasks(loggedInUser, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteTask(UUID taskId, User loggedInUser) {
        logger.info("Attempting to delete task with ID: {}", taskId);

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        if (!task.getAssignedUser().getUserId().equals(loggedInUser.getUserId())) {
            logger.warn("User {} tried to delete unauthorized task {}", loggedInUser.getEmailAddress(), taskId);
            throw new UnauthorizedAccessException("You are not authorized to delete this task");
        }

        taskRepo.delete(task);
        logger.info("Task deleted successfully with ID: {}", taskId);
    }

    private TaskDetailDto mapToResponse(Task task) {
        return TaskDetailDto.builder()
                .taskId(task.getTaskId())
                .taskTitle(task.getTaskTitle())
                .taskDescription(task.getTaskDescription())
                .taskStatus(task.getTaskStatus())
                .taskPriority(task.getTaskPriority())
                .taskDueDate(task.getTaskDueDate())
                .createdOn(task.getCreatedOn())
                .updatedOn(task.getUpdatedOn())
                .build();
    }
}