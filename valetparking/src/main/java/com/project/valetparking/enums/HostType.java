package com.project.valetparking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing the type of company/organization
 */
@Getter
@AllArgsConstructor
public enum HostType {
    INDIVIDUAL("Individual"),
    ORGANIZATION("Organization");

    private final String value;
}
