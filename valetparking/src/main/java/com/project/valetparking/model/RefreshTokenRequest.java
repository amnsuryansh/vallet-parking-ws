package com.project.valetparking.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for refreshing an access token
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    
    /**
     * The refresh token used to obtain a new access token
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}