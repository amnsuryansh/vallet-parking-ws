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
    @NotBlank(message = "Master first name is required")
    private String firstName;
    @NotBlank(message = "Master middle name is required")
    private String middleName;
    @NotBlank(message = "Master last name is required")
    private String lastName;
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    private String email;
    @NotBlank(message = "Address is required")
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private String dlNumber;
    private String designation;
}