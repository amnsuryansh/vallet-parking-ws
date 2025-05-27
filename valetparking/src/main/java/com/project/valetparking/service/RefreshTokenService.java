package com.project.valetparking.service;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.RefreshToken;

import java.util.Optional;

/**
 * Service interface for managing refresh tokens
 */
public interface RefreshTokenService {

    /**
     * Create a new refresh token for a host user
     *
     * @param hostUser the host user
     * @return the created refresh token
     */
    RefreshToken createRefreshToken(HostUser hostUser);

    /**
     * Verify if a refresh token is valid
     *
     * @param token the refresh token to verify
     * @return the verified refresh token
     */
    RefreshToken verifyExpiration(RefreshToken token);

    /**
     * Find a refresh token by its token value
     *
     * @param token the token value
     * @return an Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Revoke a specific refresh token
     *
     * @param token the token to revoke
     */
    void revokeRefreshToken(String token);

    /**
     * Revoke all refresh tokens for a specific host user
     *
     * @param hostUser the host user
     */
    void revokeAllUserTokens(HostUser hostUser);
}