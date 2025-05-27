package com.project.valetparking.service;

import com.project.valetparking.entity.HostUser;
import com.project.valetparking.model.HostUserRequest;
import com.project.valetparking.model.PasswordChangeRequest;

import java.util.List;

/**
 * Service interface for HostUser operations
 */
public interface HostUserService {

    /**
     * Create a new host user
     */
    HostUser createHostUser(Long hostId, HostUserRequest request, HostUser currentUser);

    /**
     * Get a host user by ID
     */
    HostUser getHostUserById(Long id);

    /**
     * Get a host user by username
     */
    HostUser getHostUserByUsername(String username);

    /**
     * Get all users for a host
     */
    List<HostUser> getHostUsers(Long hostId, HostUser currentUser);

    /**
     * Change password for a host user
     */
    void changePassword(Long userId, PasswordChangeRequest request, HostUser currentUser);

    /**
     * Check if the user has permission to manage other users
     */
    boolean canManageUsers(HostUser user);
}
