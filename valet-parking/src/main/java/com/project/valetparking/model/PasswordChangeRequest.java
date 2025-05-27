package com.project.valletparking.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request model for password change
 */
@Data
public class PasswordChangeRequest {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}