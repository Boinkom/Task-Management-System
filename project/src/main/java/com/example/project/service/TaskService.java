package com.example.project.service;

import com.example.project.enumiration.TaskStatus;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.repository.TasksRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public Page<Task> findTasksByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tasksRepository.findByAuthorId(authorId, pageable);
    }

    public Page<Task> findTasksByExecutor(Long executorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tasksRepository.findByExecutorId(executorId, pageable);
    }

    public Page<Task> findTasksByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tasksRepository.findByTitleContaining(title, pageable);
    }

    public List<Task> findAllTasks() {
        return tasksRepository.findAll();
    }

    private void checkExecutorAuthorization(Long executorId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (!user.getId().equals(executorId)) {
            throw new AccessDeniedException("You are not authorized to perform this action on this task");
        }
    }

    public Task updateTaskStatus(Long taskId, Long executorId, TaskStatus status) throws AccessDeniedException {
        checkExecutorAuthorization(executorId);

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setStatus(status);
        return tasksRepository.save(task);
    }

    public Task updateTaskStatusAdmin(Long taskId, Long executorId, TaskStatus status) throws AccessDeniedException {

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setStatus(status);
        return tasksRepository.save(task);
    }

    public Task addCommentToTask(Long taskId, Long executorId, String commentText) throws AccessDeniedException {
        checkExecutorAuthorization(executorId);

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new IllegalArgumentException("Executor not found"));

        commentService.addComments(taskId, commentText, executor);

        return tasksRepository.save(task);
    }

    public Task addCommentToTaskAdmin(Long taskId, Long executorId, String commentText) throws AccessDeniedException {

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new IllegalArgumentException("Executor not found"));

        commentService.addComments(taskId, commentText, executor);

        return tasksRepository.save(task);
    }


    public Task addTask(String title, String description, Long executorId, Long authorId, int priority, TaskStatus status) {

        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new IllegalArgumentException("Executor not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        task.setAuthor(author);
        task.setExecutor(executor);

        return tasksRepository.save(task);
    }

    public void deleteTask(Long taskId){

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        tasksRepository.delete(task);
    }

    public Task updateTaskPriority(Long taskId, Long executorId, int priority) throws AccessDeniedException {
        checkExecutorAuthorization(executorId);

        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setPriority(priority);
        return tasksRepository.save(task);
    }

    public Task assignExecutorToTask(Long taskId, Long executorId) throws AccessDeniedException {
        findByIdTask(taskId).setExecutor(findByIdUser(executorId));
        return tasksRepository.save(findByIdTask(taskId));
    }

    public Task findByIdTask(Long taskId) throws AccessDeniedException {
        return tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public User findByIdUser(Long executorId) throws AccessDeniedException {
        return userRepository.findById(executorId)
                .orElseThrow(() -> new IllegalArgumentException("Executor not found"));
    }

    public Task updateTask(Long taskId, String title, String description, int priority, TaskStatus status, Long executorId) throws AccessDeniedException {
        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        checkExecutorAuthorization(executorId);

        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);

        return tasksRepository.save(task);
    }

    public Task updateTaskAdmin(Long taskId, String title, String description, int priority, TaskStatus status, Long executorId) throws AccessDeniedException {
        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);

        return tasksRepository.save(task);
    }



}
