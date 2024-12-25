package com.example.project.dto;

import com.example.project.enumiration.TaskStatus;
import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private int priority;
    private TaskStatus status;
    private Long authorId;
    private Long executorId;
}
