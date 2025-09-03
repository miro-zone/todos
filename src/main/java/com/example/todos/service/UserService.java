package com.example.todos.service;

import com.example.todos.response.UserResponse;
import org.springframework.security.access.AccessDeniedException;

public interface UserService {
    UserResponse getUserInfo() throws AccessDeniedException;
    
    /**
     * Deletes the currently authenticated user.
     * If the user is an admin, it ensures they are not the last admin in the system.
     * 
     * @throws AccessDeniedException if the user is not authenticated
     * @throws IllegalStateException if the user is the last admin in the system
     */
    void deleteUser() throws AccessDeniedException;
}
