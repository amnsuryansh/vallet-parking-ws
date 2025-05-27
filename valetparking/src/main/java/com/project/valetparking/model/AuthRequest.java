package com.project.valetparking.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request model for authentication
 */
@Data
public class AuthRequest {
    @NotBlank(message = "userName is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;
}