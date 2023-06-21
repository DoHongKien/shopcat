package com.assignment.product.service;

import com.assignment.dto.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CountryService {

    private RestTemplate restTemplate;

    @Autowired
    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("countriesCache")
    public List<Country> getAllCountry() {
        ResponseEntity<Country[]> response = restTemplate.getForEntity(
                "https://restcountries.com/v3.1/all", Country[].class);

        Country[] countries = response.getBody();
        List<Country> listCountry = Arrays.asList(countries);

        listCountry.sort(Comparator.comparing(country -> country.getName().getCommon()));

        return listCountry;
    }
}
