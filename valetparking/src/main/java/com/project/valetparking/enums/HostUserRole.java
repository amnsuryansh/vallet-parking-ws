package com.project.valetparking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the roles of host users
 */
@AllArgsConstructor
@Getter
public enum HostUserRole {
    SUPERADMIN("SUPERADMIN"),
    HOSTADMIN("HOSTADMIN"),
    HOSTUSER("HOSTUSER");

    private String value;
}