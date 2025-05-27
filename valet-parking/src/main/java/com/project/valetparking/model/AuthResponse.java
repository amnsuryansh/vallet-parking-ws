package com.project.valletparking.model;

import com.project.valletparking.enums.HostUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for authentication
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long hostId;
    private String hostName;
}