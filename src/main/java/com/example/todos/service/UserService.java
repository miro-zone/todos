package com.example.todos.service;

import com.example.todos.response.UserResponse;
import org.springframework.security.access.AccessDeniedException;

public interface UserService {
    UserResponse getUserInfo() throws AccessDeniedException;
}
