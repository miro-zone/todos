package com.example.todos.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @NotEmpty(message = "Old password is required")
    private String oldPassword;

    @NotEmpty(message = "New password is required")
    @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
    private String newPassword;

    @NotEmpty(message = "Please confirm your new password")
    private String confirmNewPassword;
}
