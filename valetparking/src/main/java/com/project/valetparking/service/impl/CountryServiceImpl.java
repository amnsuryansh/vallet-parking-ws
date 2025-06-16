package com.project.valetparking.service.impl;

import com.project.valetparking.entity.Country;
import com.project.valetparking.model.CountryRequest;
import com.project.valetparking.repository.CountryRepository;
import com.project.valetparking.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public Country create(CountryRequest countryRequest) {
        //todo: add validations
        return Country.builder()
                .countryCode(countryRequest.getCountryCode())
                .countryName(countryRequest.getCountryName())
                .nationality(countryRequest.getNationality())
                .isoCode(countryRequest.getIsoCode())
                .dialCode(countryRequest.getDialCode())
                .build();
    }
}
