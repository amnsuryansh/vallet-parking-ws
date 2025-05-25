package com.project.valletparking.service.impl;

import com.project.valletparking.service.AuthService;

import com.project.valletparking.config.JwtTokenProvider;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.exception.InvalidCredentialsException;
import com.project.valletparking.model.AuthRequest;
import com.project.valletparking.model.AuthResponse;
import com.project.valletparking.repository.HostUserRepository;
import com.project.valletparking.utility.InfoMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HostUserRepository hostUserRepository;

    /**
     * Authenticate a user and generate a token
     */
    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Get user details
            HostUser user = hostUserRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new InvalidCredentialsException(InfoMessages.INVALID_CREDENTIALS));

            // Generate token
            String token = jwtTokenProvider.createToken(authentication);

            // Create response
            return new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getRole(),
                    user.getHost().getId(),
                    user.getHost().getCompanyName()
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException(InfoMessages.INVALID_CREDENTIALS);
        }
    }
}
