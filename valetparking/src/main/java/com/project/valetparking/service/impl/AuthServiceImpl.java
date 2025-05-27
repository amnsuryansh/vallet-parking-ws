package com.project.valetparking.service.impl;

import com.project.valetparking.enums.InfoType;
import com.project.valetparking.exception.ApplicationCustomException;
import com.project.valetparking.service.AuthService;

import com.project.valetparking.config.JwtTokenProvider;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.RefreshToken;
import com.project.valetparking.model.*;
import com.project.valetparking.repository.HostUserRepository;
import com.project.valetparking.service.RefreshTokenService;
import com.project.valetparking.utility.InfoMessages;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.project.valetparking.constants.ValetParkingConstants.DB_STATUS_ACTIVE;

/**
 * Implementation of AuthService for OAuth2 authentication
 * 
 * This service handles user authentication, token generation, token refresh, and logout
 * in an OAuth2 context with JWT tokens.
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HostUserRepository hostUserRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration:86400000}")
    private long accessTokenValidityInMilliseconds;

    /**
     * Authenticate a user and generate access and refresh tokens
     * 
     * @param request the authentication request containing username and password
     * @return the authentication response containing tokens and user details
     */
    @Override
    @Transactional
    public AuthResponse authenticate(AuthRequest request) {
        try {
            log.info("Authenticating user: {}", request.getUserName());

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            // Get user details
            HostUser user = hostUserRepository.findByUserNameAndStatus(request.getUserName(), DB_STATUS_ACTIVE)
                    .orElseThrow(() -> new ApplicationCustomException(InfoMessages.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN, InfoType.ERROR));

            // Determine scopes based on user role
            Set<String> scopes = determineScopes(authentication);

            // Generate access token
            String accessToken = jwtTokenProvider.createAccessToken(authentication, scopes);

            // Generate refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            // Calculate expiration time in seconds
            long expiresInSeconds = TimeUnit.MILLISECONDS.toSeconds(accessTokenValidityInMilliseconds);

            // Create response
            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken.getToken());
            response.setTokenType("Bearer");
            response.setExpiresIn(expiresInSeconds);
            response.setUsername(user.getUserName());
            response.setRole(user.getRole().getDisplayName());
            response.setHostId(user.getHost().getHostId());
            response.setHostName(user.getHost().getHostName());
            response.setScopes(scopes);

            log.info("User {} successfully authenticated", request.getUserName());
            return response;
        } catch (AuthenticationException e) {
            log.error("AuthServiceImpl::authenticate - invalid credentials with exception", e);
            throw new ApplicationCustomException(InfoMessages.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN, InfoType.ERROR);
        }
    }

    /**
     * Refresh an access token using a refresh token
     * 
     * @param request the refresh token request
     * @return the token refresh response containing the new access token
     */
    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        log.info("Processing refresh token request");

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getHostUser)
                .map(user -> {
                    // Create authentication object
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getUserName(), null, Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getDisplayName())));

                    // Determine scopes
                    Set<String> scopes = determineScopes(authentication);

                    // Generate new access token
                    String accessToken = jwtTokenProvider.createAccessToken(authentication, scopes);

                    // Calculate expiration time in seconds
                    long expiresInSeconds = TimeUnit.MILLISECONDS.toSeconds(accessTokenValidityInMilliseconds);

                    log.info("Access token refreshed for user: {}", user.getUserName());

                    // Return response with new access token and existing refresh token
                    return new TokenRefreshResponse(accessToken, requestRefreshToken, expiresInSeconds);
                })
                .orElseThrow(() -> {
                    log.error("Refresh token not found in database: {}", requestRefreshToken);
                    return new ApplicationCustomException("Refresh token is not in database!", 
                            HttpStatus.FORBIDDEN, InfoType.ERROR);
                });
    }

    /**
     * Logout a user by revoking their refresh token
     * 
     * @param refreshToken the refresh token to revoke
     */
    @Override
    @Transactional
    public void logout(String refreshToken) {
        log.info("Processing logout request");

        if (refreshToken == null) {
            log.warn("Refresh token is null");
            throw new ApplicationCustomException("Refresh token is required", 
                    HttpStatus.BAD_REQUEST, InfoType.ERROR);
        }

        refreshTokenService.revokeRefreshToken(refreshToken);
        log.info("User logged out successfully");
    }

    /**
     * Validate a token
     * 
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Determine the OAuth2 scopes for a user based on their role
     * 
     * @param authentication the authentication object
     * @return the set of scopes
     */
    private Set<String> determineScopes(Authentication authentication) {
        Set<String> scopes = new HashSet<>();

        // Basic read scope for all authenticated users
        scopes.add("read");

        // Add write scope for users with appropriate roles
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (authorities.contains("ROLE_HOST_MASTER") || 
            authorities.contains("ROLE_HOST_ADMIN")) {
            scopes.add("write");
        }

        // Add admin scope for host masters
        if (authorities.contains("ROLE_HOST_MASTER")) {
            scopes.add("admin");
        }

        return scopes;
    }

    /**
     * Simple GrantedAuthority implementation
     */
    private static class SimpleGrantedAuthority implements GrantedAuthority {
        private final String authority;

        public SimpleGrantedAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
}
