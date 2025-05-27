package com.project.valetparking.controller;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.UserRole;
import com.project.valetparking.model.ResponseMessage;
import com.project.valetparking.service.HostUserService;
import com.project.valetparking.service.ResponseMessageService;
import com.project.valetparking.service.UserRoleService;
import com.project.valetparking.utility.InfoMessages;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user-role")
@AllArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final HostUserService hostUserService;
    private final ResponseMessageService responseMessageService;

    /**
     * Create a new host user
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('HOST_MASTER', 'HOST_ADMIN')")
    public ResponseEntity<ResponseMessage<UserRole>> createHostUser(
            @RequestParam String name,
            @RequestParam String displayName,
            @RequestParam String description,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        // Create user role
        UserRole userRole = userRoleService.create(name, displayName, description);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(userRole, InfoMessages.USER_ROLE_CREATED));
    }

}
