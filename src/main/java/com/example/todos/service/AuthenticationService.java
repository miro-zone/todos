package com.example.todos.service;

import com.example.todos.request.AuthenticationRequest;
import com.example.todos.request.RegisterRequest;
import com.example.todos.response.AuthenticationResponse;

public interface AuthenticationService  {
    void register(RegisterRequest request) throws Exception;
    AuthenticationResponse login(AuthenticationRequest request);
}
