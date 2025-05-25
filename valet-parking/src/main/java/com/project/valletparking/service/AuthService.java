package com.project.valletparking.service;

import com.project.valletparking.model.AuthRequest;
import com.project.valletparking.model.AuthResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Authenticate a user and generate a token
     */
    AuthResponse authenticate(AuthRequest request);
}
