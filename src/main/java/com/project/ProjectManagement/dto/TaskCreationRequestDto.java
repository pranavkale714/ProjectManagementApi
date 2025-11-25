package com.example.projectmanagement.dto;


import com.project.ProjectManagement.enums.Priority;
import com.project.ProjectManagement.enums.Status;
import com.project.ProjectManagement.model.Task;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationRequestDto {

    private String taskTitle;
    private String taskDescription;
    private Status taskStatus;
    private Priority taskPriority;
    private LocalDate taskDueDate;

    public Task toEntity() {
        return Task.builder()
                .taskTitle(this.taskTitle)
                .taskDescription(this.taskDescription)
                .taskStatus(this.taskStatus)
                .taskPriority(this.taskPriority)
                .taskDueDate(this.taskDueDate)
                .build();
    }
}
