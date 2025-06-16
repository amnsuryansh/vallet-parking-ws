package com.project.valetparking.controller;

import com.project.valetparking.enums.InfoType;
import com.project.valetparking.model.*;
import com.project.valetparking.service.AuthService;
import com.project.valetparking.service.ResponseMessageService;
import com.project.valetparking.utility.InfoMessages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for OAuth2 authentication endpoints
 * <p>
 * This controller provides endpoints for user authentication, token refresh, and logout
 * in an OAuth2 context with JWT tokens.
 */
@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final ResponseMessageService responseMessageService;

    /**
     * Login endpoint
     * <p>
     * This endpoint authenticates a user with a username and password
     * and returns access and refresh tokens along with user details.
     * 
     * @param request the authentication request containing username and password
     * @return the authentication response containing tokens and user details
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        log.info("Login request received for user: {}", request.getUserName());
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createResponse(authResponse, InfoType.SUCCESS, InfoMessages.LOGIN_SUCCESS));
    }

    /**
     * Token refresh endpoint
     * <p>
     * This endpoint refreshes an access token using a refresh token.
     * It returns a new access token while keeping the same refresh token.
     * 
     * @param request the refresh token request
     * @return the token refresh response containing the new access token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseMessage<TokenRefreshResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Token refresh request received");
        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createResponse(tokenRefreshResponse, InfoType.SUCCESS, "Token refreshed successfully"));
    }

    /**
     * Logout endpoint
     * <p>
     * This endpoint logs out a user by revoking their refresh token.
     * After logout, the refresh token can no longer be used to get new access tokens.
     * 
     * @param request the refresh token request
     * @return a success message
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage<String>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Logout request received");
        authService.logout(request.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createResponse("Logged out successfully", InfoType.SUCCESS, "Logout successful"));
    }

    /**
     * Token validation endpoint
     * <p>
     * This endpoint validates a token and returns whether it is valid or not.
     * 
     * @param token the token to validate
     * @return a message indicating if the token is valid
     */
    @GetMapping("/validate-token")
    public ResponseEntity<ResponseMessage<Boolean>> validateToken(@RequestParam String token) {
        log.info("Token validation request received");
        boolean isValid = authService.validateToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createResponse(isValid, InfoType.SUCCESS, 
                        isValid ? "Token is valid" : "Token is invalid"));
    }
}
