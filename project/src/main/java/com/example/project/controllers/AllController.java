package com.example.project.controllers;

import com.example.project.dto.UserDTO;
import com.example.project.models.Task;
import com.example.project.models.User;
import com.example.project.service.TaskService;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0/")
@Tag(name = "AllController", description = "Контроллер для работы с задачами, связанными с авторами, исполнителями и названиями.")
public class AllController {
    private final TaskService taskService;
    private final UserService userService;

    /**
     * Получить задачи по автору.
     *
     * @param authorId ID автора, задачи которого нужно найти.
     * @param page     Номер страницы (по умолчанию 0).
     * @param size     Размер страницы (по умолчанию 10).
     * @return Страница задач, созданных указанным автором.
     */
    @GetMapping("/tasks/author")
    @Operation(summary = "Получить задачи по автору", description = "Возвращает список задач, созданных определенным автором.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен."),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос."),
            @ApiResponse(responseCode = "404", description = "Автор не найден.")
    })
    public Page<Task> getTasksByAuthor(
            @RequestParam @Parameter(description = "ID автора, задачи которого нужно найти.") Long authorId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы, начиная с 0.") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов на странице.") int size) {
        return taskService.findTasksByAuthor(authorId, page, size);
    }

    /**
     * Получить задачи по исполнителю.
     *
     * @param executorId ID исполнителя, задачи которого нужно найти.
     * @param page       Номер страницы (по умолчанию 0).
     * @param size       Размер страницы (по умолчанию 10).
     * @return Страница задач, назначенных указанному исполнителю.
     */
    @GetMapping("/tasks/executor")
    @Operation(summary = "Получить задачи по исполнителю", description = "Возвращает список задач, назначенных определенному исполнителю.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен."),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос."),
            @ApiResponse(responseCode = "404", description = "Исполнитель не найден.")
    })
    public Page<Task> getTasksByExecutor(
            @RequestParam @Parameter(description = "ID исполнителя, задачи которого нужно найти.") Long executorId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы, начиная с 0.") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов на странице.") int size) {
        return taskService.findTasksByExecutor(executorId, page, size);
    }

    /**
     * Получить задачи по названию.
     *
     * @param title Название задачи для поиска.
     * @param page  Номер страницы (по умолчанию 0).
     * @param size  Размер страницы (по умолчанию 10).
     * @return Страница задач с указанным названием.
     */
    @GetMapping("/tasks/title")
    @Operation(summary = "Получить задачи по названию", description = "Возвращает список задач, содержащих указанное название.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен."),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос."),
            @ApiResponse(responseCode = "404", description = "Задачи с указанным названием не найдены.")
    })
    public Page<Task> getTasksByTitle(
            @RequestParam @Parameter(description = "Название задачи для поиска.") String title,
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы, начиная с 0.") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество элементов на странице.") int size) {
        return taskService.findTasksByTitle(title, page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.addUser(userDTO));
    }
}
