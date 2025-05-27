package com.project.valetparking.service.impl;

import com.project.valetparking.entity.UserRole;
import com.project.valetparking.enums.InfoType;
import com.project.valetparking.exception.ApplicationCustomException;
import com.project.valetparking.repository.UserRoleRepository;
import com.project.valetparking.service.UserRoleService;
import com.project.valetparking.utility.InfoMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.project.valetparking.constants.ValetParkingConstants.DB_STATUS_ACTIVE;

@Service
@Slf4j
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRole create(String name, String displayName, String description) {
        UserRole role = userRoleRepository.findByDisplayNameAndStatus(displayName, DB_STATUS_ACTIVE);
        if (role == null) {
            role = new UserRole();
            role.setName(name);
            role.setDisplayName(displayName);
            role.setDescription(description);
            return userRoleRepository.save(role);
        }
        throw new ApplicationCustomException(InfoMessages.USER_ROLE_FOUND, HttpStatus.BAD_GATEWAY, InfoType.ERROR);
    }
}
