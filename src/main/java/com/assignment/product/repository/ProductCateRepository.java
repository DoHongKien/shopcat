package com.assignment.product.repository;

import com.assignment.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCateRepository extends JpaRepository<ProductCategory, Integer> {
}
