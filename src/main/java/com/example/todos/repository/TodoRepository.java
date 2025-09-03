package com.example.todos.repository;

import com.example.todos.entity.Todo;
import com.example.todos.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    List<Todo> findByOwner(User owner);
    Optional<Todo> findByIdAndOwner(Long id, User owner);
}
