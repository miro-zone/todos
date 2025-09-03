package com.example.todos.service;

import com.example.todos.request.RegisterRequest;

public interface AuthenticationService  {
    void register(RegisterRequest request) throws Exception;
}
