package com.project.valetparking.controller;

import com.project.valetparking.entity.Host;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.model.HostRegistrationRequest;
import com.project.valetparking.model.ResponseMessage;
import com.project.valetparking.service.HostService;
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

/**
 * Controller for host-related endpoints in OAuth2 context
 * 
 * This controller provides endpoints for host registration and management.
 * The registration endpoint is public, while other endpoints require authentication
 * and appropriate scopes.
 */
@RestController
@RequestMapping("/v1/admin/host")
@AllArgsConstructor
@Slf4j
public class HostController {

    private final HostService hostService;
    private final HostUserService hostUserService;
    private final ResponseMessageService responseMessageService;

    /**
     * Register a new host with a master user
     * 
     * This endpoint allows registering a new host organization along with its master user.
     * The process:
     * 1. Creates a new host entity with the provided details
     * 2. Creates a host user with HOST_MASTER role
     * 3. Sets up the user's credentials for authentication
     * 
     * This endpoint is public and doesn't require authentication.
     * 
     * @param request the host registration request containing host and user details
     * @return the created host entity
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<Host>> registerHost(@Valid @RequestBody HostRegistrationRequest request) {
        log.info("Host registration request received for: {}", request.getHostName());
        Host host = hostService.registerHost(request);
        log.info("Host registered successfully with ID: {}", host.getHostId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(host, InfoMessages.HOST_CREATED));
    }

    /**
     * Get host details
     * 
     * This endpoint retrieves details of a specific host.
     * It requires authentication and checks if the user has access to the requested host.
     * 
     * Required scope: read
     * 
     * @param hostId the ID of the host to retrieve
     * @param userDetails the authenticated user details
     * @return the host entity
     */
    @GetMapping("/{hostId}")
//    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN') or hasAuthority('ROLE_HOSTADMIN')")
    public ResponseEntity<ResponseMessage<Host>> getHost(
            @PathVariable Long hostId,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Get host request received for host ID: {}", hostId);

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        log.debug("User {} requesting host details", currentUser.getUserName());

        // Check if user has access to the host
        hostService.hasHostAccess(currentUser, hostId);
        log.debug("Access verified for user {} to host {}", currentUser.getUserName(), hostId);

        // Get host
        Host host = hostService.getHostById(hostId);
        log.info("Host details retrieved successfully for host ID: {}", hostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(host, "Host details retrieved successfully"));
    }

}
