package com.project.valetparking.service;

import com.project.valetparking.model.AuthRequest;
import com.project.valetparking.model.AuthResponse;
import com.project.valetparking.model.RefreshTokenRequest;
import com.project.valetparking.model.TokenRefreshResponse;

/**
 * Service interface for OAuth2 authentication operations
 * 
 * This interface defines methods for user authentication, token refresh, and logout
 * in an OAuth2 context with JWT tokens.
 */
public interface AuthService {

    /**
     * Authenticate a user and generate access and refresh tokens
     * 
     * @param request the authentication request containing username and password
     * @return the authentication response containing tokens and user details
     */
    AuthResponse authenticate(AuthRequest request);

    /**
     * Refresh an access token using a refresh token
     * 
     * @param request the refresh token request
     * @return the token refresh response containing the new access token
     */
    TokenRefreshResponse refreshToken(RefreshTokenRequest request);

    /**
     * Logout a user by revoking their refresh tokens
     * 
     * @param refreshToken the refresh token to revoke
     */
    void logout(String refreshToken);

    /**
     * Validate a token
     * 
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}
