package com.project.valletparking.service.impl;

import com.project.valletparking.enums.InfoType;
import com.project.valletparking.exception.ApplicationCustomException;
import com.project.valletparking.service.AuthService;

import com.project.valletparking.config.JwtTokenProvider;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.model.AuthRequest;
import com.project.valletparking.model.AuthResponse;
import com.project.valletparking.repository.HostUserRepository;
import com.project.valletparking.utility.InfoMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static com.project.valletparking.constants.ValletParkingConstants.DB_STATUS_ACTIVE;

/**
 * Implementation of AuthService
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

    /**
     * Authenticate a user and generate a token
     */
    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            // Get user details
            HostUser user = hostUserRepository.findByUserNameAndStatus(request.getUserName(), DB_STATUS_ACTIVE)
                    .orElseThrow(() -> new ApplicationCustomException(InfoMessages.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN, InfoType.ERROR));

            // Generate token
            String token = jwtTokenProvider.createToken(authentication);

            // Create response
            return new AuthResponse(
                    token,
                    user.getUserName(),
                    user.getRole().getDisplayName(),
                    user.getHost().getHostId(),
                    user.getHost().getHostName()
            );
        } catch (AuthenticationException e) {
            log.error("AuthServiceImpl::authenticate - invalid credentials with exception", e);
            throw new ApplicationCustomException(InfoMessages.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN, InfoType.ERROR);
        }
    }
}
