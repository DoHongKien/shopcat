package com.assignment.product.service;

import com.assignment.entity.ProductCategory;
import com.assignment.product.repository.ProductCateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCateService implements IProductCateService{

    @Autowired
    private ProductCateRepository repository;

    @Override
    @Cacheable(value = "productCategories", keyGenerator = "keyGenerator")
    public List<ProductCategory> findAllPrductCate() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "productCategories", key = "#id")
    public ProductCategory findById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    @CacheEvict(value = "productCategories::ProductCateService:findAllPrductCate", allEntries = true)
    public ProductCategory saveProductCate(ProductCategory productCategory) {
        return repository.save(productCategory);
    }

    @Override
    @CacheEvict(value = "productCategories::ProductCateService:findAllPrductCate", allEntries = true)
    public void deleteProductCate(Integer id) {
        repository.deleteById(id);
    }
}
