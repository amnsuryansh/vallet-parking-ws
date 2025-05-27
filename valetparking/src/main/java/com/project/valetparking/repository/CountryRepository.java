package com.project.valetparking.repository;

import com.project.valetparking.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface CountryRepository extends JpaRepository<Country, Serializable> {

    Country findByCountryCode(String countryCode);

}