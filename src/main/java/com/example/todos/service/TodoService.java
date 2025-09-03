package com.example.todos.service;

import com.example.todos.request.TodoRequest;
import com.example.todos.response.TodoResponse;

public interface TodoService {
    TodoResponse createTodo(TodoRequest request);
}
