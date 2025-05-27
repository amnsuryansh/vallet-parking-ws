package com.project.valetparking.service;

import com.project.valetparking.entity.Host;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.model.HostRegistrationRequest;
/**
 * Service interface for Host operations
 */
public interface HostService {

    /**
     * Register a new host with a master user
     */
    Host registerHost(HostRegistrationRequest request);

    /**
     * Get a host by ID
     */
    Host getHostById(Long id);

    /**
     * Check if the user has access to the host
     */
    boolean hasHostAccess(HostUser user, Long hostId);

}