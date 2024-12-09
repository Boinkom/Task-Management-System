package com.example.project.service;

import com.example.project.enumiration.TaskStatus;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.repository.TasksRepository;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User executor;

    @Mock
    private Task task;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(authentication.getName()).thenReturn("executorUsername");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addTask_shouldCreateTask() {
        Long executorId = 1L;
        Long authorId = 2L;
        String title = "Task Title";
        String description = "Task Description";
        TaskStatus status = TaskStatus.NEW;
        int priority = 1;

        when(userRepository.findById(executorId)).thenReturn(Optional.of(executor));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(executor));
        when(tasksRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.addTask(title, description, executorId, authorId, priority, status);

        verify(tasksRepository, times(1)).save(any(Task.class));
        assertNotNull(createdTask);
    }

    @Test
    void deleteTask_shouldRemoveTask() {
        Long taskId = 1L;

        when(tasksRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(tasksRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenTaskNotFound() {
        Long taskId = 1L;

        when(tasksRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(taskId));
    }
}
