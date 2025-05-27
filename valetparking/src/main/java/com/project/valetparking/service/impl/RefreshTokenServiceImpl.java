package com.project.valetparking.service.impl;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.RefreshToken;
import com.project.valetparking.enums.InfoType;
import com.project.valetparking.exception.ApplicationCustomException;
import com.project.valetparking.repository.RefreshTokenRepository;
import com.project.valetparking.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of RefreshTokenService for managing refresh tokens
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    /**
     * Create a new refresh token for a host user
     * 
     * @param hostUser the host user
     * @return the created refresh token
     */
    @Override
    public RefreshToken createRefreshToken(HostUser hostUser) {
        // Create a new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setHostUser(hostUser);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);
        
        // Save and return the refresh token
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Verify if a refresh token is valid
     * 
     * @param token the refresh token to verify
     * @return the verified refresh token
     * @throws ApplicationCustomException if the token is expired or revoked
     */
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        // Check if the token is expired
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new ApplicationCustomException("Refresh token was expired. Please make a new login request", 
                    HttpStatus.FORBIDDEN, InfoType.ERROR);
        }
        
        // Check if the token is revoked
        if (token.isRevoked()) {
            throw new ApplicationCustomException("Refresh token was revoked. Please make a new login request", 
                    HttpStatus.FORBIDDEN, InfoType.ERROR);
        }
        
        return token;
    }

    /**
     * Find a refresh token by its token value
     * 
     * @param token the token value
     * @return an Optional containing the refresh token if found
     */
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Revoke a specific refresh token
     * 
     * @param token the token to revoke
     */
    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            RefreshToken tokenToRevoke = refreshToken.get();
            tokenToRevoke.setRevoked(true);
            refreshTokenRepository.save(tokenToRevoke);
        }
    }

    /**
     * Revoke all refresh tokens for a specific host user
     * 
     * @param hostUser the host user
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(HostUser hostUser) {
        refreshTokenRepository.revokeAllUserTokens(hostUser);
    }
}