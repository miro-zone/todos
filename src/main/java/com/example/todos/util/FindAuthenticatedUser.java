package com.example.todos.util;

import com.example.todos.entity.User;
import org.springframework.security.access.AccessDeniedException;

public interface FindAuthenticatedUser {
    User getAuthenticatedUser() throws AccessDeniedException;
}
