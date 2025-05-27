package com.project.valetparking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for token refresh operations
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {
    
    /**
     * The new access token
     */
    private String accessToken;
    
    /**
     * The refresh token (same as the one used for the request)
     */
    private String refreshToken;
    
    /**
     * The token type (always "Bearer")
     */
    private String tokenType = "Bearer";
    
    /**
     * The expiration time of the access token in seconds
     */
    private long expiresIn;
    
    /**
     * Constructor with access token and refresh token
     * 
     * @param accessToken the new access token
     * @param refreshToken the refresh token
     * @param expiresIn the expiration time in seconds
     */
    public TokenRefreshResponse(String accessToken, String refreshToken, long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}