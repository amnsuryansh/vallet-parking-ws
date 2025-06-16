package com.project.valetparking.model;

import com.project.valetparking.enums.HostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * Request model for host registration
 */
@Data
public class HostRegistrationRequest implements Serializable {
    @NotBlank(message = "Host name is required")
    private String hostName;
    @NotNull(message = "Company type is required")
    private HostType hostType;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotNull(message = "Email is required")
    private String hostEmail;
    @NotBlank(message = "Address is required")
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private Long hostSize;
    private String instagramProfileUrl;
    private String twitterProfileUrl;
    private String linkedInProfileUrl;
    @NotBlank(message = "user name is required")
    private String username;
    // Master user details
    @NotBlank(message = "Master first name is required")
    private String masterFirstName;
    private String masterMiddleName;
    private String masterLastName;
    @NotBlank(message = "Master password is required")
    private String masterPassword;
    @NotBlank(message = "Master phone number is required")
    private String masterPhoneNumber;
    @NotNull(message = "number of parking slots at host are required")
    private Long parkingSlots;
    private String website;
    private String gstInNumber;
    private String registrationNumber;
    private String designation;
    private String dlNumber;
}