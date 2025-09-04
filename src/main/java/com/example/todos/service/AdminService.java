package com.example.todos.service;

import com.example.todos.response.UserResponse;
import java.util.List;

public interface AdminService {
    List<UserResponse> getAllUsers();
    UserResponse promoteToAdmin(Long userId);
    void deleteNonAdminUser(Long userId);
}
