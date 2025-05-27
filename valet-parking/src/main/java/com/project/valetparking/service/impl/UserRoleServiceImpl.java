package com.project.valletparking.service.impl;

import com.project.valletparking.entity.UserRole;
import com.project.valletparking.enums.InfoType;
import com.project.valletparking.exception.ApplicationCustomException;
import com.project.valletparking.repository.UserRoleRepository;
import com.project.valletparking.service.UserRoleService;
import com.project.valletparking.utility.InfoMessages;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.project.valletparking.constants.ValletParkingConstants.DB_STATUS_ACTIVE;

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
