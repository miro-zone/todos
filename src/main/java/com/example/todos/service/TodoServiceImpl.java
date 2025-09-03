package com.example.todos.service;

import com.example.todos.entity.Todo;
import com.example.todos.entity.User;
import com.example.todos.repository.TodoRepository;
import com.example.todos.request.TodoRequest;
import com.example.todos.response.TodoResponse;
import com.example.todos.util.FindAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;

    @Override
    @Transactional
    public TodoResponse createTodo(TodoRequest request) {
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
        
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .complete(false)
                .owner(currentUser)
                .build();
        
        Todo savedTodo = todoRepository.save(todo);
        
        return mapToResponse(savedTodo);
    }
    
    private TodoResponse mapToResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .priority(todo.getPriority())
                .completed(todo.isComplete())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
