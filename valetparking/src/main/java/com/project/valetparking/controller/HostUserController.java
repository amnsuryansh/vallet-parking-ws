package com.project.valetparking.controller;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.model.HostUserRequest;
import com.project.valetparking.model.PasswordChangeRequest;
import com.project.valetparking.model.ResponseMessage;
import com.project.valetparking.service.HostUserService;
import com.project.valetparking.service.ResponseMessageService;
import com.project.valetparking.utility.InfoMessages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for host user-related endpoints in OAuth2 context
 * 
 * This controller provides endpoints for managing host users, including:
 * - Creating new host users (admin/employee)
 * - Retrieving user information
 * - Changing passwords
 * 
 * Access to these endpoints is controlled by OAuth2 scopes and user roles.
 */
@RestController
@RequestMapping("/api/host-users")
@AllArgsConstructor
@Slf4j
public class HostUserController {

    private HostUserService hostUserService;
    private ResponseMessageService responseMessageService;

    /**
     * Create a new host user
     * 
     * This endpoint allows creating a new host user (admin or employee).
     * The process:
     * 1. Validates that the current user has permission to create users
     * 2. Creates the user with the specified role
     * 3. Sends an email for password setup
     * 
     * Required scope: write
     * Required roles: HOST_MASTER or HOST_ADMIN
     * 
     * @param hostId the ID of the host to create the user for
     * @param request the user creation request containing user details
     * @param userDetails the authenticated user details
     * @return the created host user entity
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_write') and hasAnyRole('HOST_MASTER', 'HOST_ADMIN')")
    public ResponseEntity<ResponseMessage<HostUser>> createHostUser(
            @RequestParam Long hostId,
            @Valid @RequestBody HostUserRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Create host user request received for host ID: {}", hostId);

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        log.debug("User {} creating new host user", currentUser.getUserName());

        // Create host user
        HostUser hostUser = hostUserService.createHostUser(hostId, request, currentUser);
        log.info("Host user created successfully with ID: {}", hostUser.getHostUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(hostUser, InfoMessages.HOST_USER_CREATED));
    }

    /**
     * Get all users for a host
     * 
     * This endpoint retrieves all users associated with a specific host.
     * It checks if the current user has access to the host.
     * 
     * Required scope: read
     * 
     * @param hostId the ID of the host to get users for
     * @param userDetails the authenticated user details
     * @return a list of host user entities
     */
    @GetMapping("/host/{hostId}")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public ResponseEntity<ResponseMessage<List<HostUser>>> getHostUsers(
            @PathVariable Long hostId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Get host users request received for host ID: {}", hostId);

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        log.debug("User {} requesting host users", currentUser.getUserName());

        // Get host users
        List<HostUser> hostUsers = hostUserService.getHostUsers(hostId, currentUser);
        log.info("Retrieved {} host users for host ID: {}", hostUsers.size(), hostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(hostUsers, "Host users retrieved successfully"));
    }

    /**
     * Change password
     * 
     * This endpoint allows a user to change their password or an admin to change another user's password.
     * It verifies that the current user has permission to change the specified user's password.
     * 
     * Required scope: write
     * 
     * @param userId the ID of the user to change password for
     * @param request the password change request
     * @param userDetails the authenticated user details
     * @return a success message
     */
    @PostMapping("/{userId}/change-password")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    public ResponseEntity<ResponseMessage<Void>> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Change password request received for user ID: {}", userId);

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        log.debug("User {} changing password for user {}", currentUser.getUserName(), userId);

        // Change password
        hostUserService.changePassword(userId, request, currentUser);
        log.info("Password changed successfully for user ID: {}", userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(null, InfoMessages.PASSWORD_CHANGED));
    }

    /**
     * Get current user details
     * 
     * This endpoint retrieves the details of the currently authenticated user.
     * 
     * Required scope: read
     * 
     * @param userDetails the authenticated user details
     * @return the current user entity
     */
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public ResponseEntity<ResponseMessage<HostUser>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Get current user details request received");

        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        log.info("Current user details retrieved successfully for user: {}", currentUser.getUserName());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(currentUser, "Current user details retrieved successfully"));
    }
}
