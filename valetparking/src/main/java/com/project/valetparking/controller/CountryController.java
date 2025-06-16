package com.project.valetparking.controller;


import com.project.valetparking.entity.Country;
import com.project.valetparking.entity.HostUser;
import com.project.valetparking.entity.UserRole;
import com.project.valetparking.model.CountryRequest;
import com.project.valetparking.model.ResponseMessage;
import com.project.valetparking.service.CountryService;
import com.project.valetparking.service.HostUserService;
import com.project.valetparking.service.ResponseMessageService;
import com.project.valetparking.utility.InfoMessages;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/country")
@AllArgsConstructor
public class CountryController {

    private final HostUserService hostUserService;
    private final ResponseMessageService responseMessageService;
    private final CountryService countryService;

    /**
     * Create a new country
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('HOST_MASTER', 'HOST_ADMIN')")
    public ResponseEntity<ResponseMessage<Country>> createCountry(
            @RequestBody CountryRequest countryRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Get current user
        HostUser currentUser = hostUserService.getHostUserByUsername(userDetails.getUsername());
        // Create country
        Country country = countryService.create(countryRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseMessageService.createSuccessResponse(country, InfoMessages.COUNTRY_CREATED));
    }

}
