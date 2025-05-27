package com.project.valletparking.controller;

import com.project.valletparking.entity.HostUser;
import com.project.valletparking.model.HostUserRequest;
import com.project.valletparking.model.PasswordChangeRequest;
import com.project.valletparking.model.ResponseMessage;
import com.project.valletparking.service.HostUserService;
import com.project.valletparking.service.ResponseMessageService;
import com.project.valletparking.utility.InfoMessages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for host user-related endpoints
 */
@RestController
@RequestMapping("/v1/host-users")
@AllArgsConstructor
public class HostUserController {

    private HostUserService hostUserService;
    private ResponseMessageService responseMessageService;

    /**
     * Create a new host user
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('HOST_MASTER', 'HOST_ADMIN')")
    public ResponseEntity<ResponseMessage<HostUser>> createHostUser(
            @RequestParam Long hostId,
            @Valid @RequestBody HostUserRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());

        // Create host user
        HostUser hostUser = hostUserService.createHostUser(hostId, request, currentUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(hostUser, InfoMessages.HOST_USER_CREATED));
    }

    /**
     * Get all users for a host
     */
    @GetMapping("/host/{hostId}")
    public ResponseEntity<ResponseMessage<List<HostUser>>> getHostUsers(
            @PathVariable Long hostId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());

        // Get host users
        List<HostUser> hostUsers = hostUserService.getHostUsers(hostId, currentUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(hostUsers, "Host users retrieved successfully"));
    }

    /**
     * Change password
     */
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<ResponseMessage<Void>> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());

        // Change password
        hostUserService.changePassword(userId, request, currentUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(null, InfoMessages.PASSWORD_CHANGED));
    }

    /**
     * Get current user details
     */
    @GetMapping("/me")
    public ResponseEntity<ResponseMessage<HostUser>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(currentUser, "Current user details retrieved successfully"));
    }
}
