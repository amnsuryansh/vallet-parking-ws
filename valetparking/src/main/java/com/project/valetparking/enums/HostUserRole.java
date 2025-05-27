package com.project.valetparking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the roles of host users
 */
@AllArgsConstructor
@Getter
public enum HostUserRole {
    HOST_MASTER("HOST_MASTER"),
    HOST_ADMIN("HOST_ADMIN"),
    HOST_EMPLOYEE("HOST_EMPLOYEE");

    private String value;
}