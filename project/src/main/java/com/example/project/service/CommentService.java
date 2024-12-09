package com.example.project.service;

import com.example.project.models.Comment;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.repository.CommentsRepository;
import com.example.project.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentsRepository commentsRepository;
    private final TasksRepository tasksRepository;

    public void addComments(Long taskId, String commentText, User executor) {
        if (taskId == null || commentText == null || commentText.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input data: taskId and commentText must not be null or empty");
        }

        if (executor == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Executor cannot be null");
        }

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with id " + taskId + " not found"));

        Comment comment = new Comment();
        comment.setContent(commentText);
        comment.setTask(task);
        comment.setAuthor(executor);

        commentsRepository.save(comment);
    }
}
