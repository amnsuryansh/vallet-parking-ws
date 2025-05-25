package com.project.valletparking.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request model for authentication
 */
@Data
public class AuthRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}