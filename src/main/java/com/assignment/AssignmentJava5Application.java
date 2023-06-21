package com.assignment;

import com.assignment.product.service.CountryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssignmentJava5Application {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentJava5Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(CountryService countryService) {
        return commanRunner -> countryService.getAllCountry();
    }
}
