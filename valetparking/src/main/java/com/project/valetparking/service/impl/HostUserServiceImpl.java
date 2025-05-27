package com.project.valetparking.service.impl;

import com.project.valetparking.entity.Host;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.enums.HostUserRole;
import com.project.valetparking.enums.InfoType;
import com.project.valetparking.exception.ApplicationCustomException;
import com.project.valetparking.model.HostUserRequest;
import com.project.valetparking.model.PasswordChangeRequest;
import com.project.valetparking.repository.CountryRepository;
import com.project.valetparking.repository.HostUserRepository;
import com.project.valetparking.repository.UserRoleRepository;
import com.project.valetparking.service.HostService;
import com.project.valetparking.service.HostUserService;
import com.project.valetparking.utility.InfoMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.project.valetparking.constants.ValetParkingConstants.DB_STATUS_ACTIVE;
import static com.project.valetparking.enums.HostUserRole.HOST_ADMIN;

/**
 * Implementation of HostUserService
 */
@Service
@Slf4j
@AllArgsConstructor
public class HostUserServiceImpl implements HostUserService {

    private final HostUserRepository hostUserRepository;
    private final HostService hostService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private CountryRepository countryRepository;

    /**
     * Create a new host user
     */
    @Override
    public HostUser createHostUser(Long hostId, HostUserRequest request, HostUser currentUser) {
        // Check if user has access to the host
        hostService.hasHostAccess(currentUser, hostId);

        // Check if username already exists
        if (hostUserRepository.existsByUserNameAndStatus(request.getUserName(), DB_STATUS_ACTIVE)) {
            throw new IllegalArgumentException(InfoMessages.USERNAME_EXISTS);
        }

        // Get host
        Host host = hostService.getHostById(hostId);

        // Create new host user
        HostUser hostUser = new HostUser();
        hostUser.setRole(userRoleRepository.findByDisplayNameAndStatus(HOST_ADMIN.getValue(), DB_STATUS_ACTIVE));
        hostUser.setEmail(request.getEmail());
        hostUser.setFirstName(request.getFirstName());
        hostUser.setMiddleName(request.getMiddleName());
        hostUser.setLastName(request.getLastName());
        hostUser.setUserName(request.getUserName());
        hostUser.setPassword(passwordEncoder.encode(request.getPassword()));
        hostUser.setDesignation(request.getDesignation());
        hostUser.setContactNumber(request.getPhoneNumber());
        hostUser.setAddressLine1(request.getAddressLine1());
        hostUser.setAddressLine2(request.getAddressLine2());
        hostUser.setCity(request.getCity());
        hostUser.setState(request.getState());
        hostUser.setPostalCode(request.getPostalCode());
        hostUser.setCountry(countryRepository.findByCountryCode(request.getCountryCode()));
        hostUser.setDlNumber(request.getDlNumber());
        hostUser.setHost(host);

        return hostUserRepository.save(hostUser);
    }

    /**
     * Get a host user by ID
     */
    @Override
    public HostUser getHostUserById(Long id) {
        return hostUserRepository.findById(id)
                .orElseThrow(() -> new ApplicationCustomException(InfoMessages.HOST_USER_NOT_FOUND, HttpStatus.NOT_FOUND, InfoType.ERROR));
    }

    /**
     * Get a host user by username
     */
    @Override
    public HostUser getHostUserByUsername(String username) {
        return hostUserRepository.findByUserNameAndStatus(username, DB_STATUS_ACTIVE)
                .orElseThrow(() -> new ApplicationCustomException(InfoMessages.HOST_USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND, InfoType.ERROR));
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
        return hostUserRepository.findByHostAndStatus(host, DB_STATUS_ACTIVE);
    }

    /**
     * Change password for a host user
     */
    @Override
    public void changePassword(Long userId, PasswordChangeRequest request, HostUser currentUser) {
        // Check if it's the user's own password or if they have permission to change others' passwords
        if (!currentUser.getHostUserId().equals(userId)) {
            throw new ApplicationCustomException(InfoMessages.UNAUTHORIZED_ACTION, HttpStatus.FORBIDDEN, InfoType.ERROR);
        }

        // Get the user whose password is being changed
        HostUser user = getHostUserById(userId);

        // Check if the user belongs to the same host
        if (!user.getHost().getHostId().equals(currentUser.getHost().getHostId())) {
            throw new ApplicationCustomException(InfoMessages.UNAUTHORIZED_ACTION, HttpStatus.FORBIDDEN, InfoType.ERROR);
        }

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ApplicationCustomException(InfoMessages.INCORRECT_PASSWORD, HttpStatus.INTERNAL_SERVER_ERROR, InfoType.ERROR);

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
        return Objects.equals(user.getRole().getDisplayName(), HostUserRole.HOST_MASTER.getValue())
                || Objects.equals(user.getRole().getDisplayName(), HostUserRole.HOST_ADMIN.getValue());
    }
}