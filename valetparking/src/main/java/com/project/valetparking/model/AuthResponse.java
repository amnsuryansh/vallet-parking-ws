package com.project.valetparking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Response model for OAuth2 authentication
 * 
 * This class represents the response sent to clients after successful authentication,
 * including access token, refresh token, and user details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    /**
     * The JWT access token
     */
    private String accessToken;

    /**
     * The JWT refresh token
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
     * The username of the authenticated user
     */
    private String username;

    /**
     * The role of the authenticated user
     */
    private String role;

    /**
     * The host ID of the authenticated user
     */
    private Long hostId;

    /**
     * The host name of the authenticated user
     */
    private String hostName;

    /**
     * The scopes granted to the user
     */
    private Set<String> scopes;

    /**
     * Constructor with token and user details (for backward compatibility)
     */
    public AuthResponse(String token, String username, String role, Long hostId, String hostName) {
        this.accessToken = token;
        this.username = username;
        this.role = role;
        this.hostId = hostId;
        this.hostName = hostName;
    }
}
