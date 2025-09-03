package com.example.todos.controller;

import com.example.todos.request.UpdatePasswordRequest;
import com.example.todos.response.UserResponse;
import com.example.todos.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Update password", 
        description = "Updates the password of the currently authenticated user. " +
                     "The old password must match the current password, the new password " +
                     "must be different from the old one, and the new password must match " +
                     "the confirmation."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204", 
            description = "Password was successfully updated"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request - possible reasons: " +
                         "Old password is incorrect, " +
                         "New password is the same as old password, " +
                         "New password and confirmation don't match",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "User not authenticated - authentication required"
        )
    })
    public void updatePassword(
        @Parameter(description = "Password update details", required = true)
        @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
    }
}
