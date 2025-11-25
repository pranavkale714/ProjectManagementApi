package com.project.ProjectManagement.repository;

import com.project.ProjectManagement.enums.Priority;
import com.project.ProjectManagement.enums.Status;
import com.project.ProjectManagement.model.Task;
import com.project.ProjectManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends JpaRepository<Task, UUID> {

    // Get all tasks assigned to a user
    List<Task> findByAssignedUser(User user);

    // Search tasks by title or description for a user
    @Query("SELECT t FROM Task t WHERE t.assignedUser = :user AND " +
            "(LOWER(t.taskTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.taskDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchUserTasks(@Param("user") User user, @Param("keyword") String keyword);

    // Filter tasks by status or priority for a user
    List<Task> findByAssignedUserAndTaskStatus(User user, Status status);
    List<Task> findByAssignedUserAndTaskPriority(User user, Priority priority);

}
