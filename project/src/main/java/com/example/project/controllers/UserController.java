package com.example.project.controllers;

import com.example.project.enumiration.TaskStatus;
import com.example.project.models.Task;
import com.example.project.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/")
@Tag(name = "UserController", description = "Контроллер для работы с задачами пользователя.")
public class UserController {

    private final TaskService taskService;

    /**
     * Пример запроса для пользователей с правами 'ADMIN' или 'USER'.
     *
     * @return Строка с текстом.
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Получить информацию для пользователя", description = "Доступ для пользователей с правами 'ADMIN' и 'USER'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ответ с информацией для пользователя."),
            @ApiResponse(responseCode = "403", description = "Отказ в доступе.")
    })
    public String userStuff() {
        return "User stuff";
    }

    /**
     * Обновить статус задачи.
     *
     * @param taskId    ID задачи.
     * @param executorId ID исполнителя.
     * @param status     Новый статус задачи.
     * @return Обновленную задачу.
     * @throws AccessDeniedException если у пользователя нет прав для изменения статуса задачи.
     */
    @PostMapping("/{taskId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Обновить статус задачи", description = "Обновляет статус задачи для пользователя или администратора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен."),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос."),
            @ApiResponse(responseCode = "403", description = "Отказ в доступе.")
    })
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam Long executorId,
            @RequestParam TaskStatus status) throws AccessDeniedException {
        Task updatedTask = taskService.updateTaskStatus(taskId, executorId, status);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Добавить комментарий к задаче.
     *
     * @param taskId    ID задачи.
     * @param executorId ID исполнителя.
     * @param comment    Текст комментария.
     * @return Обновленную задачу с добавленным комментарием.
     * @throws AccessDeniedException если у пользователя нет прав для добавления комментария.
     */
    @PostMapping("/{taskId}/comments")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @Operation(summary = "Добавить комментарий к задаче", description = "Добавляет комментарий к задаче для пользователя или администратора.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен."),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос."),
            @ApiResponse(responseCode = "403", description = "Отказ в доступе.")
    })
    public ResponseEntity<Task> addCommentToTask(
            @PathVariable Long taskId,
            @RequestParam Long executorId,
            @RequestParam String comment) throws AccessDeniedException {
        Task updatedTask = taskService.addCommentToTask(taskId, executorId, comment);
        return ResponseEntity.ok(updatedTask);
    }
}
