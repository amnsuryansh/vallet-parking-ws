package com.project.valletparking.service;

import com.project.valletparking.entity.UserRole;

public interface UserRoleService {
    UserRole create(String name, String displayName, String description);
}
