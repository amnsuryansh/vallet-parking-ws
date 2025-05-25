package com.project.valletparking.model;

import com.project.valletparking.enums.HostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * Request model for host registration
 */
@Data
public class HostRegistrationRequest implements Serializable {
    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Company type is required")
    private HostType hostType;

    @NotNull(message = "Email is required")
    private String email;
    private String website;
    private String taxId;
    private String registrationNumber;

    // Master user details
    @NotBlank(message = "Master user name is required")
    private String masterName;

    @NotBlank(message = "Master username is required")
    private String masterUsername;

    @NotBlank(message = "Master password is required")
    private String masterPassword;

    @NotBlank(message = "Master address is required")
    private String masterAddress;

    @NotBlank(message = "Master phone number is required")
    private String masterPhoneNumber;

    private String masterEmail;
}