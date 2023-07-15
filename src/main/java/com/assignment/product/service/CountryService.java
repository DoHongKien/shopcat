package com.assignment.product.service;

import com.assignment.dto.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final RestTemplate restTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CountryService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Cacheable(value = "countriesCache", keyGenerator = "keyGenerator")
    public List<Country> getAllCountry() {

        List<Country> listCountry;

        if (redisTemplate.hasKey("countriesCache")) {
            List<Object> countries = redisTemplate.opsForList().range("countriesCache", 0, -1);

            listCountry = countries.stream()
                    .map(obj -> (Country) obj)
                    .collect(Collectors.toList());

            listCountry.sort(Comparator.comparing(country -> country.getName().getCommon()));
        } else {
            ResponseEntity<Country[]> response = restTemplate.getForEntity(
                    "https://restcountries.com/v3.1/all", Country[].class);

            Country[] countries = response.getBody();
            listCountry = Arrays.asList(countries);

            listCountry.sort(Comparator.comparing(country -> country.getName().getCommon()));
        }

        return listCountry;
    }
}
