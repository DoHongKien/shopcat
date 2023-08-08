package com.assignment.product.service;

import com.assignment.entity.ProductCategory;

import java.util.List;

public interface IProductCateService {

    List<ProductCategory> findAllProductCate();

    ProductCategory findById(Integer id);

    ProductCategory saveProductCate(ProductCategory productCategory);

    void deleteProductCate(Integer id);
}
