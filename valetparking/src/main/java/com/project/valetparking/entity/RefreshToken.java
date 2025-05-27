package com.project.valetparking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Entity representing a refresh token for OAuth2 authentication
 * Refresh tokens are used to obtain new access tokens without requiring the user to re-authenticate
 */
@EqualsAndHashCode(callSuper = true)
@Entity(name = "refresh_token")
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The refresh token value
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The expiration time of the refresh token
     */
    @Column(nullable = false)
    private Instant expiryDate;

    /**
     * The user associated with this refresh token
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostUserId", nullable = false, referencedColumnName = "hostUserId")
    private HostUser hostUser;

    /**
     * Flag indicating if the token has been revoked
     */
    @Column(nullable = false)
    private boolean revoked = false;
}