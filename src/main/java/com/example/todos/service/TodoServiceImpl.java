package com.example.todos.service;

import com.example.todos.entity.Todo;
import com.example.todos.entity.User;
import com.example.todos.repository.TodoRepository;
import com.example.todos.request.TodoRequest;
import com.example.todos.response.TodoResponse;
import com.example.todos.util.FindAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    
    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getAllTodos() {
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
        return todoRepository.findByOwner(currentUser).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TodoResponse toggleTodoCompletion(Long todoId) {
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
        Todo todo = todoRepository.findByIdAndOwner(todoId, currentUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        
        todo.setComplete(!todo.isComplete());
        Todo updatedTodo = todoRepository.save(todo);
        return mapToResponse(updatedTodo);
    }

    @Override
    @Transactional
    public void deleteTodo(Long todoId) {
        User currentUser = findAuthenticatedUser.getAuthenticatedUser();
        Todo todo = todoRepository.findByIdAndOwner(todoId, currentUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
        
        todoRepository.delete(todo);
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
