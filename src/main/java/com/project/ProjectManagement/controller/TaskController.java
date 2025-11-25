package com.project.ProjectManagement.controller;

import com.project.ProjectManagement.dto.TaskDeletionResponseDto;
import com.project.ProjectManagement.dto.TaskDetailDto;
import com.project.ProjectManagement.enums.Priority;
import com.project.ProjectManagement.enums.Status;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import com.project.ProjectManagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepo userRepository;


    private User fetchCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        return userRepository.findByEmailAddress(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/project/{projId}")
    public ResponseEntity<TaskDetailDto> addTask(
            @PathVariable UUID projId,
            @RequestBody com.example.projectmanagement.dto.TaskCreationRequestDto requestDto) {

        User currentUser = fetchCurrentUser();
        TaskDetailDto createdTask = taskService.createTask(projId, requestDto, currentUser);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDetailDto> modifyTask(
            @PathVariable UUID taskId,
            @RequestBody com.example.projectmanagement.dto.TaskCreationRequestDto requestDto) {

        User currentUser = fetchCurrentUser();
        TaskDetailDto updatedTask = taskService.updateTask(taskId, requestDto, currentUser);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskDetailDto>> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String sortBy) {

        User currentUser = fetchCurrentUser();
        List<TaskDetailDto> tasks = taskService.listTasks(currentUser, status, priority, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDetailDto>> filterTasks(@RequestParam String keyword) {
        User currentUser = fetchCurrentUser();
        List<TaskDetailDto> results = taskService.searchTasks(currentUser, keyword);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskDeletionResponseDto> removeTask(@PathVariable UUID taskId) {
        User currentUser = fetchCurrentUser();
        taskService.deleteTask(taskId, currentUser);

        TaskDeletionResponseDto response = new TaskDeletionResponseDto(
                "Task removed successfully",
                taskId,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}