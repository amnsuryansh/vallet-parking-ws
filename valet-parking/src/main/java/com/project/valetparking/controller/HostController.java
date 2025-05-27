package com.project.valletparking.controller;

import com.project.valletparking.entity.Host;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.model.HostRegistrationRequest;
import com.project.valletparking.model.ResponseMessage;
import com.project.valletparking.service.HostService;
import com.project.valletparking.service.HostUserService;
import com.project.valletparking.service.ResponseMessageService;
import com.project.valletparking.utility.InfoMessages;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for host-related endpoints
 */
@RestController
@RequestMapping("/v1/hosts")
@AllArgsConstructor
public class HostController {

    private final HostService hostService;
    private final HostUserService hostUserService;
    private final ResponseMessageService responseMessageService;

    /**
     * Register a new host with a master user
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<Host>> registerHost(@Valid @RequestBody HostRegistrationRequest request) {
        Host host = hostService.registerHost(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(host, InfoMessages.HOST_CREATED));
    }

    /**
     * Get host details
     */
    @GetMapping("/{hostId}")
    public ResponseEntity<ResponseMessage<Host>> getHost(
            @PathVariable Long hostId,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());

        // Check if user has access to the host
        hostService.hasHostAccess(currentUser, hostId);

        // Get host
        Host host = hostService.getHostById(hostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMessageService.createSuccessResponse(host, "Host details retrieved successfully"));
    }
}
