package com.project.valletparking.service.impl;

import com.project.valletparking.service.HostService;

import com.project.valletparking.entity.Host;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.enums.HostUserRole;
import com.project.valletparking.exception.EntityNotFoundException;
import com.project.valletparking.exception.UnauthorizedException;
import com.project.valletparking.model.HostRegistrationRequest;
import com.project.valletparking.repository.HostRepository;
import com.project.valletparking.repository.HostUserRepository;
import com.project.valletparking.utility.InfoMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of HostService
 */
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private HostUserRepository hostUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new host with a master user
     */
    @Override
    @Transactional
    public Host registerHost(HostRegistrationRequest request) {
        // Check if company name already exists
        if (hostRepository.existsByCompanyName(request.getCompanyName())) {
            throw new IllegalArgumentException(InfoMessages.COMPANY_NAME_EXISTS);
        }

        // Check if username already exists
        if (hostUserRepository.existsByUsername(request.getMasterUsername())) {
            throw new IllegalArgumentException(InfoMessages.USERNAME_EXISTS);
        }

        // Create and save host
        Host host = new Host();
        host.setCompanyName(request.getCompanyName());
        host.setAddress(request.getAddress());
        host.setPhoneNumber(request.getPhoneNumber());
        host.setCompanyType(request.getCompanyType());
        host.setEmail(request.getEmail());
        host.setWebsite(request.getWebsite());
        host.setTaxId(request.getTaxId());
        host.setRegistrationNumber(request.getRegistrationNumber());

        Host savedHost = hostRepository.save(host);

        // Create master user
        HostUser masterUser = new HostUser();
        masterUser.setName(request.getMasterName());
        masterUser.setUsername(request.getMasterUsername());
        masterUser.setPassword(passwordEncoder.encode(request.getMasterPassword()));
        masterUser.setAddress(request.getMasterAddress());
        masterUser.setPhoneNumber(request.getMasterPhoneNumber());
        masterUser.setEmail(request.getMasterEmail());
        masterUser.setRole(HostUserRole.HOST_MASTER);
        masterUser.setHost(savedHost);

        hostUserRepository.save(masterUser);

        return savedHost;
    }

    /**
     * Get a host by ID
     */
    @Override
    public Host getHostById(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(InfoMessages.HOST_NOT_FOUND));
    }

    /**
     * Check if the user has access to the host
     */
    @Override
    public boolean hasHostAccess(HostUser user, Long hostId) {
        // Check if the user belongs to the requested host
        if (!user.getHost().getId().equals(hostId)) {
            throw new UnauthorizedException(InfoMessages.UNAUTHORIZED_ACTION);
        }
        return true;
    }
}
