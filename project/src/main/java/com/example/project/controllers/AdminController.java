package com.example.project.controllers;

import com.example.project.dto.TaskDTO;
import com.example.project.enumiration.TaskStatus;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.service.TaskService;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Контроллер для управления задачами и пользователями с административными полномочиями.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/")
@Tag(name = "Admin Controller", description = "Контроллер для управления задачами и пользователями администраторами.")
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    /**
     * Получить список всех задач.
     *
     * @return Список всех задач.
     */
    @GetMapping("/findAllTask")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получить список всех задач", description = "Возвращает полный список задач, доступный администраторам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен.")
    })
    public List<Task> findAllTask() {
        return taskService.findAllTasks();
    }

    /**
     * Получить список всех пользователей.
     *
     * @return Список всех пользователей.
     */
    @GetMapping("/findAllUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает полный список пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен.")
    })
    public List<User> findAllUser() {
        return userService.findAll();
    }

    /**
     * Добавить новую задачу.
     *
     * @param taskDTO DTO с данными задачи.
     * @return Созданная задача.
     */
    @PostMapping("/addTask")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Добавить задачу", description = "Создает новую задачу с указанными параметрами.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен.")
    })
    public ResponseEntity<Task> addTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.addTask(
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getExecutorId(),
                taskDTO.getAuthorId(),
                taskDTO.getPriority(),
                taskDTO.getStatus()
        );
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    /**
     * Удалить задачу по ID.
     *
     * @param taskId Идентификатор задачи.
     */
    @PostMapping("/delete/{taskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно удалена."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача не найдена.")
    })
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    /**
     * Обновить приоритет задачи.
     *
     * @param taskId     Идентификатор задачи.
     * @param executorId Идентификатор исполнителя.
     * @param priority   Новый приоритет.
     * @return Обновленная задача.
     * @throws AccessDeniedException Если доступ запрещен.
     */
    @PutMapping("/updateTaskPriority/{taskId}/{executorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновить приоритет задачи", description = "Обновляет приоритет указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приоритет задачи успешно обновлен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача не найдена.")
    })
    public ResponseEntity<Task> updateTaskPriority(
            @PathVariable Long taskId,
            @PathVariable Long executorId,
            @RequestParam int priority
    ) throws AccessDeniedException {
        Task updatedTask = taskService.updateTaskPriority(taskId, executorId, priority);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Обновить задачу.
     *
     * @param taskId  Идентификатор задачи.
     * @param taskDTO DTO с новыми данными задачи.
     * @return Обновленная задача.
     * @throws AccessDeniedException Если доступ запрещен.
     */
    @PutMapping("/updateTask/{taskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновить задачу", description = "Обновляет данные задачи, включая название, описание, приоритет, статус и исполнителя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача не найдена.")
    })
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) throws AccessDeniedException {
        Task updatedTask = taskService.updateTaskAdmin(
                taskId,
                taskDTO.getTitle(),
                taskDTO.getDescription(),
                taskDTO.getPriority(),
                taskDTO.getStatus(),
                taskDTO.getExecutorId()
        );
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Обновить статус задачи.
     *
     * @param taskId     Идентификатор задачи.
     * @param executorId Идентификатор исполнителя.
     * @param status     Новый статус задачи.
     * @return Обновленная задача.
     * @throws AccessDeniedException Если доступ запрещен.
     */
    @PutMapping("/updateTaskStatus/{taskId}/{executorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновить статус задачи", description = "Обновляет статус указанной задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача не найдена.")
    })
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @PathVariable Long executorId,
            @RequestParam TaskStatus status
    ) throws AccessDeniedException {
        Task updatedTask = taskService.updateTaskStatusAdmin(taskId, executorId, status);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Назначить исполнителя на задачу.
     *
     * @param taskId     Идентификатор задачи.
     * @param executorId Идентификатор исполнителя.
     * @return Обновленная задача с назначенным исполнителем.
     * @throws AccessDeniedException Если доступ запрещен.
     */
    @PutMapping("/assignExecutorToTask/{taskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Назначить исполнителя на задачу", description = "Назначает указанного исполнителя на задачу.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Исполнитель успешно назначен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача или исполнитель не найдены.")
    })
    public ResponseEntity<Task> assignExecutorToTask(
            @PathVariable Long taskId,
            @RequestBody Long executorId
    ) throws AccessDeniedException {
        Task updatedTask = taskService.assignExecutorToTask(taskId, executorId);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    /**
     * Добавить комментарий к задаче.
     *
     * @param taskId      Идентификатор задачи.
     * @param executorId  Идентификатор исполнителя.
     * @param commentText Текст комментария.
     * @return Обновленная задача с добавленным комментарием.
     * @throws AccessDeniedException Если доступ запрещен.
     */
    @PostMapping("/addCommentToTask/{taskId}/{executorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Добавить комментарий к задаче", description = "Добавляет комментарий к указанной задаче от имени исполнителя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен."),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен."),
            @ApiResponse(responseCode = "404", description = "Задача или исполнитель не найдены.")
    })
    public ResponseEntity<Task> addCommentToTask(
            @PathVariable Long taskId,
            @PathVariable Long executorId,
            @RequestBody String commentText
    ) throws AccessDeniedException {
        Task updatedTask = taskService.addCommentToTaskAdmin(taskId, executorId, commentText);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }
}

