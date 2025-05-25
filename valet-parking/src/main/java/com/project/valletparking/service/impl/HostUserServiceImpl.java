package com.project.valletparking.service.impl;

import com.project.valletparking.entity.Host;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.enums.HostUserRole;
import com.project.valletparking.exception.EntityNotFoundException;
import com.project.valletparking.exception.InvalidCredentialsException;
import com.project.valletparking.exception.UnauthorizedException;
import com.project.valletparking.model.HostUserRequest;
import com.project.valletparking.model.PasswordChangeRequest;
import com.project.valletparking.repository.HostUserRepository;
import com.project.valletparking.service.HostService;
import com.project.valletparking.utility.InfoMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of HostUserService
 */
@Service
public class HostUserServiceImpl implements HostUserService {

    @Autowired
    private HostUserRepository hostUserRepository;

    @Autowired
    private HostService hostService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new host user
     */
    @Override
    public HostUser createHostUser(Long hostId, HostUserRequest request, HostUser currentUser) {
        // Check if user has permission to create users
        if (!canManageUsers(currentUser)) {
            throw new UnauthorizedException(InfoMessages.UNAUTHORIZED_ACTION);
        }

        // Check if user has access to the host
        hostService.hasHostAccess(currentUser, hostId);

        // Check if username already exists
        if (hostUserRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(InfoMessages.USERNAME_EXISTS);
        }

        // Get host
        Host host = hostService.getHostById(hostId);

        // Create new host user
        HostUser hostUser = new HostUser();
        hostUser.setName(request.getName());
        hostUser.setUsername(request.getUsername());
        hostUser.setPassword(passwordEncoder.encode(request.getPassword()));
        hostUser.setAddress(request.getAddress());
        hostUser.setPhoneNumber(request.getPhoneNumber());
        hostUser.setEmail(request.getEmail());
        hostUser.setRole(request.getRole());
        hostUser.setHost(host);
        hostUser.setEmergencyContact(request.getEmergencyContact());
        hostUser.setIdProofType(request.getIdProofType());
        hostUser.setIdProofNumber(request.getIdProofNumber());

        return hostUserRepository.save(hostUser);
    }

    /**
     * Get a host user by ID
     */
    @Override
    public HostUser getHostUserById(Long id) {
        return hostUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(InfoMessages.HOST_USER_NOT_FOUND));
    }

    /**
     * Get a host user by username
     */
    @Override
    public HostUser getHostUserByUsername(String username) {
        return hostUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(InfoMessages.HOST_USER_NOT_FOUND));
    }

    /**
     * Get all users for a host
     */
    @Override
    public List<HostUser> getHostUsers(Long hostId, HostUser currentUser) {
        // Check if user has access to the host
        hostService.hasHostAccess(currentUser, hostId);

        // Get host
        Host host = hostService.getHostById(hostId);

        // Return all users for the host
        return hostUserRepository.findByHost(host);
    }

    /**
     * Change password for a host user
     */
    @Override
    public void changePassword(Long userId, PasswordChangeRequest request, HostUser currentUser) {
        // Check if it's the user's own password or if they have permission to change others' passwords
        if (!currentUser.getId().equals(userId) && !canManageUsers(currentUser)) {
            throw new UnauthorizedException(InfoMessages.UNAUTHORIZED_ACTION);
        }

        // Get the user whose password is being changed
        HostUser user = getHostUserById(userId);

        // Check if the user belongs to the same host
        if (!user.getHost().getId().equals(currentUser.getHost().getId())) {
            throw new UnauthorizedException(InfoMessages.UNAUTHORIZED_ACTION);
        }

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(InfoMessages.INCORRECT_PASSWORD);
        }

        // Check if new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(InfoMessages.PASSWORDS_DONT_MATCH);
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        hostUserRepository.save(user);
    }

    /**
     * Check if the user has permission to manage other users
     */
    @Override
    public boolean canManageUsers(HostUser user) {
        return user.getRole() == HostUserRole.HOST_MASTER || user.getRole() == HostUserRole.HOST_ADMIN;
    }
}