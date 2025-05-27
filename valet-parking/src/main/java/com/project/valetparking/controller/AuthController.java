package com.project.valletparking.controller;

import com.project.valletparking.enums.InfoType;
import com.project.valletparking.model.AuthRequest;
import com.project.valletparking.model.AuthResponse;
import com.project.valletparking.model.ResponseMessage;
import com.project.valletparking.service.AuthService;
import com.project.valletparking.service.ResponseMessageService;
import com.project.valletparking.utility.InfoMessages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResponseMessageService responseMessageService;

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createResponse(authResponse, InfoType.SUCCESS, InfoMessages.LOGIN_SUCCESS));
    }
}
