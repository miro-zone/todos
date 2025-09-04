package com.example.todos.controller;

import com.example.todos.response.UserResponse;
import com.example.todos.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Admin-only operations")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users/{id}/promote")
    @Operation(summary = "Promote user to admin")
    public ResponseEntity<UserResponse> promoteToAdmin(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(adminService.promoteToAdmin(userId));
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a non-admin user")
    public void deleteNonAdminUser(@PathVariable("id") Long userId) {
        adminService.deleteNonAdminUser(userId);
    }
}
