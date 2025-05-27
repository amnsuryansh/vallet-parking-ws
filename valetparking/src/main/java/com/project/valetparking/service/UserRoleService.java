package com.project.valetparking.service;

import com.project.valetparking.entity.UserRole;

public interface UserRoleService {
    UserRole create(String name, String displayName, String description);
}
