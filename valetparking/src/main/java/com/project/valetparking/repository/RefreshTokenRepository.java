package com.project.valetparking.repository;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing RefreshToken entities
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    /**
     * Find a refresh token by its token value
     * 
     * @param token the token value
     * @return an Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Find all refresh tokens for a specific host user
     * 
     * @param hostUser the host user
     * @return a list of refresh tokens
     */
    List<RefreshToken> findByHostUser(HostUser hostUser);
    
    /**
     * Find all active (non-revoked) refresh tokens for a specific host user
     * 
     * @param hostUser the host user
     * @param revoked the revoked status (false for active tokens)
     * @return a list of active refresh tokens
     */
    List<RefreshToken> findByHostUserAndRevoked(HostUser hostUser, boolean revoked);
    
    /**
     * Delete all refresh tokens for a specific host user
     * 
     * @param hostUser the host user
     */
    void deleteByHostUser(HostUser hostUser);
    
    /**
     * Revoke all refresh tokens for a specific host user
     * 
     * @param hostUser the host user
     * @return the number of tokens revoked
     */
    @Modifying
    @Query("UPDATE refresh_token r SET r.revoked = true WHERE r.hostUser = ?1 AND r.revoked = false")
    int revokeAllUserTokens(HostUser hostUser);
}