package com.assignment;

import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.sell.service.InvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AssignmentJava5Application {

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    public static void main(String[] args) {
        SpringApplication.run(AssignmentJava5Application.class, args);
    }

}
