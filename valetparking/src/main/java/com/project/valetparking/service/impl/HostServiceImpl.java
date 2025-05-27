package com.project.valetparking.service.impl;

import com.project.valetparking.enums.InfoType;
import com.project.valetparking.exception.ApplicationCustomException;
import com.project.valetparking.repository.CountryRepository;
import com.project.valetparking.repository.UserRoleRepository;
import com.project.valetparking.service.HostService;

import com.project.valetparking.entity.Host;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.model.HostRegistrationRequest;
import com.project.valetparking.repository.HostRepository;
import com.project.valetparking.repository.HostUserRepository;
import com.project.valetparking.utility.InfoMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.valetparking.constants.ValetParkingConstants.DB_STATUS_ACTIVE;
import static com.project.valetparking.enums.HostUserRole.HOST_ADMIN;

/**
 * Implementation of HostService
 */
@Service
@Slf4j
@AllArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final HostUserRepository hostUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * Register a new host with a master user
     */
    @Override
    @Transactional
    public Host registerHost(HostRegistrationRequest request) {
        // Check if company name already exists
        if (hostRepository.existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException(InfoMessages.HOST_NAME_EXISTS);
        }

        // Create and save host
        Host host = new Host();
        host.setHostType(request.getHostType());
        host.setHostName(request.getHostName());
        host.setEmail(request.getHostEmail());
        host.setAddressLine1(request.getAddressLine1());
        host.setAddressLine2(request.getAddressLine2());
        host.setCity(request.getCity());
        host.setState(request.getState());
        host.setPostalCode(request.getPostalCode());
        host.setCountryCode(request.getCountryCode());
        host.setCountry(countryRepository.findByCountryCode(request.getCountryCode()));
        host.setWebsite(request.getWebsite());
        host.setHostSize(request.getHostSize());
        host.setInstagramProfileUrl(request.getInstagramProfileUrl());
        host.setTwitterProfileUrl(request.getTwitterProfileUrl());
        host.setLinkedInProfileUrl(request.getLinkedInProfileUrl());
        host.setGstInNumber(request.getGstInNumber());
        host.setPhoneNumber(request.getPhoneNumber());
        host.setParkingSlots(request.getParkingSlots());

        Host savedHost = hostRepository.save(host);

        // Create master user
        HostUser masterUser = new HostUser();
        masterUser.setRole(userRoleRepository.findByDisplayNameAndStatus(HOST_ADMIN.getValue(), DB_STATUS_ACTIVE));
        masterUser.setEmail(request.getHostEmail());
        masterUser.setFirstName(request.getMasterFirstName());
        masterUser.setMiddleName(request.getMasterMiddleName());
        masterUser.setLastName(request.getMasterLastName());
        masterUser.setUserName(request.getUsername());
        masterUser.setPassword(passwordEncoder.encode(request.getMasterPassword()));
        masterUser.setDesignation(request.getDesignation());
        masterUser.setContactNumber(request.getMasterPhoneNumber());
        masterUser.setAddressLine1(request.getAddressLine1());
        masterUser.setAddressLine2(request.getAddressLine2());
        masterUser.setCity(request.getCity());
        masterUser.setState(request.getState());
        masterUser.setPostalCode(request.getPostalCode());
        masterUser.setCountry(countryRepository.findByCountryCode(request.getCountryCode()));
        masterUser.setDlNumber(request.getDlNumber());
        masterUser.setHost(savedHost);

        hostUserRepository.save(masterUser);

        //todo: email verification  -by otp or link?

        return savedHost;
    }

    /**
     * Get a host by ID
     */
    @Override
    public Host getHostById(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new ApplicationCustomException(InfoMessages.HOST_NOT_FOUND, HttpStatus.NOT_FOUND, InfoType.ERROR));
    }

    /**
     * Check if the user has access to the host
     */
    @Override
    public boolean hasHostAccess(HostUser user, Long hostId) {
        // Check if the user belongs to the requested host
        if (!user.getHost().getHostId().equals(hostId)) {
            throw new ApplicationCustomException(InfoMessages.UNAUTHORIZED_ACTION, HttpStatus.FORBIDDEN, InfoType.ERROR);
        }
        return true;
    }

}
