package com.example.todos.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public record UserResponse(
    long id,
    String fullName,
    String email,
    Collection<? extends GrantedAuthority> authorities
) {}
