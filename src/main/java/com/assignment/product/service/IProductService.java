package com.assignment.product.service;

import com.assignment.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    List<Product> findAll();

    Page<Product> findByPage(int pageNum, String keyword);

    Page<Product> findProductByPriceBetween(int pageNum, BigDecimal min, BigDecimal max);

    Page<Product> findProductByNameContainsAndPriceBetween(int pageNum, String name, BigDecimal min, BigDecimal max);

    List<Product> findProductInStock();

    Page<Product> findByCategoryName(int pageNum, String keyword);

    Product findById(int id);

    Product saveProduct(Product product);

    void deleteProduct(int id);

    void updateQtyProduct(int id, int qty);

    void updateDiscountPriceInProduct(Integer id, BigDecimal discountPrice);
}
