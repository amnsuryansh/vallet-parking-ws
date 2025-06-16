package com.project.valetparking.service;

import com.project.valetparking.entity.Country;
import com.project.valetparking.model.CountryRequest;

public interface CountryService {
    Country create(CountryRequest countryRequest);
}
