package com.project.valletparking.model;

import com.project.valletparking.enums.HostUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * Request model for host user creation
 */
@Data
public class HostUserRequest implements Serializable {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String email;

    @NotNull(message = "Role is required")
    private HostUserRole role;

    private String emergencyContact;
    private String idProofType;
    private String idProofNumber;
}