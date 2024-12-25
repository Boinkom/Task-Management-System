package com.example.project.service;

import com.example.project.models.Comment;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.repository.CommentsRepository;
import com.example.project.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private User executor;

    @Mock
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addComments_shouldSaveComment() {
        Long taskId = 1L;
        String commentText = "Test Comment";

        when(tasksRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentsRepository.save(any(Comment.class))).thenReturn(new Comment());

        commentService.addComments(taskId, commentText, executor);

        verify(commentsRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComments_shouldThrowExceptionWhenTaskNotFound() {
        Long taskId = 1L;
        String commentText = "Test Comment";

        when(tasksRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> commentService.addComments(taskId, commentText, executor));
    }
}
