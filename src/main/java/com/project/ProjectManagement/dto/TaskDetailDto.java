package com.project.ProjectManagement.dto;


import com.project.ProjectManagement.enums.Priority;
import com.project.ProjectManagement.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetailDto {

    private UUID taskId;
    private String taskTitle;
    private String taskDescription;
    private Status taskStatus;
    private Priority taskPriority;
    private LocalDate taskDueDate;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
