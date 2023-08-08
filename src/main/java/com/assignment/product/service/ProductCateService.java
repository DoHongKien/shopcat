package com.assignment.product.service;

import com.assignment.entity.ProductCategory;
import com.assignment.product.repository.ProductCateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCateService implements IProductCateService {

    @Autowired
    private ProductCateRepository repository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Cacheable(value = "productCategories", key = "#root.methodName")
    public List<ProductCategory> findAllProductCate() {
        return repository.findAll();
    }

    public List<Object> getCachedProductCategories(long start, long end) {
        String key = "productCategories::findAllProductCate"; // Key tương ứng với cache
        return redisTemplate.opsForList().range(key, start, end);

//
//        if (redisTemplate.hasKey(key)) {
//            return (List<ProductCategory>) redisTemplate.opsForValue().get(key);
//        }
//
//        // Nếu không tìm thấy giá trị trong Redis, bạn có thể thực hiện việc lấy lại dữ liệu và đặt nó vào Redis.
//        List<ProductCategory> productCategories = findAllProductCate();
//        redisTemplate.opsForValue().set(key, productCategories);
//
//        return productCategories;
    }

    @Override
    @Cacheable(value = "productCategories", key = "#id")
    public ProductCategory findById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    @CacheEvict(value = "productCategories::ProductCateService:findAllProductCate", allEntries = true)
    public ProductCategory saveProductCate(ProductCategory productCategory) {
        return repository.save(productCategory);
    }

    @Override
    @CacheEvict(value = "productCategories::ProductCateService:findAllProductCate", allEntries = true)
    public void deleteProductCate(Integer id) {
        repository.deleteById(id);
    }
}
