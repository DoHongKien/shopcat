package com.assignment.product.service;

import com.assignment.entity.ProductCategory;
import com.assignment.product.repository.ProductCateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCateService implements IProductCateService{

    @Autowired
    private ProductCateRepository repository;

    @Override
    public List<ProductCategory> findAllPrductCate() {
        return repository.findAll();
    }

    @Override
    public ProductCategory findById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public ProductCategory saveProductCate(ProductCategory productCategory) {
        return repository.save(productCategory);
    }

    @Override
    public void deleteProductCate(Integer id) {
        repository.deleteById(id);
    }
}
