package com.example.todos.controller;

import com.example.todos.response.UserResponse;
import com.example.todos.service.UserService;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    @Operation(summary = "Get current user info", description = "Retrieves information about the currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user info"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<UserResponse> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }
    
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete current user", description = "Deletes the currently authenticated user. If the user is an admin, ensures they are not the last admin in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Cannot delete the last admin user"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public void deleteUser() {
        userService.deleteUser();
    }
}
