package com.assignment.product.controller;

import com.assignment.product.service.ProductCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductCategoryRestController {

    @Autowired
    private ProductCateService productCateService;

    @GetMapping("/redis-data")
    public ResponseEntity<List<Object>> getListProductCategory() {

        List<Object> category = productCateService.getCachedProductCategories(0, 2);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
