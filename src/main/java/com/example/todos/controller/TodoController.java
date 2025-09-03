package com.example.todos.controller;

import com.example.todos.request.TodoRequest;
import com.example.todos.response.TodoResponse;
import com.example.todos.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todo Management", description = "APIs for managing todos")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    @Operation(
        summary = "Get all todos for current user",
        description = "Retrieves all todos for the currently authenticated user"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved todos"
    )
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new todo",
        description = "Creates a new todo for the current user"
    )
    @ApiResponse(responseCode = "201", description = "Todo created successfully")
    public TodoResponse createTodo(@Valid @RequestBody TodoRequest request) {
        return todoService.createTodo(request);
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(
        summary = "Toggle todo completion status",
        description = "Toggles the completion status of a todo"
    )
    @ApiResponse(responseCode = "200", description = "Todo completion status toggled successfully")
    @ApiResponse(responseCode = "404", description = "Todo not found")
    public TodoResponse toggleTodoCompletion(@PathVariable Long id) {
        return todoService.toggleTodoCompletion(id);
    }
}
