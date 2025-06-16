package com.project.valetparking.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryRequest implements Serializable {
    @NotEmpty(message = "Country code is required")
    private String countryCode;
    @NotEmpty(message = "Country name is required")
    private String countryName;
    @NotEmpty(message = "Nationality is required")
    private String nationality;
    @NotEmpty(message = "isoCode is required")
    private String isoCode;
    @NotEmpty(message = "Dial Code is required")
    private String dialCode;
}
