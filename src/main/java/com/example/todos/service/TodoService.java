package com.example.todos.service;

import com.example.todos.request.TodoRequest;
import com.example.todos.response.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse createTodo(TodoRequest request);
    List<TodoResponse> getAllTodos();
    TodoResponse toggleTodoCompletion(Long todoId);
    
    void deleteTodo(Long todoId);
}
