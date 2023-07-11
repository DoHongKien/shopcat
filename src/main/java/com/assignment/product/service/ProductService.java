package com.assignment.product.service;

import com.assignment.dto.ProductContainsPromotion;
import com.assignment.entity.Product;
import com.assignment.product.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private static final int PRODUCT_PER_PAGE = 3;

    private static final int SEARCH_CATEGORY_PER_PAGE = 9;

    private IProductRepository productRepository;

    @Autowired
    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findByPage(int pageNum, String keyword) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
        Page<Product> productPages = productRepository.findAll(pageable);
        if (keyword != null) {
            return productRepository.findByKeyword(keyword, pageable);
        }
        return productPages;
    }

    @Override
    public Page<Product> findProductByPriceBetween(int pageNum, BigDecimal min, BigDecimal max) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
        Page<Product> productPages = productRepository.findAll(pageable);
        if (min != null && max != null) {
            return productRepository.findProductByPriceBetween(min, max, pageable);
        }
        return productPages;
    }

    @Override
    public Page<Product> findProductByNameContainsAndPriceBetween(int pageNum, String name, BigDecimal min, BigDecimal max) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
        Page<Product> productPages = productRepository.findAll(pageable);

        if (name != null && min != null && max != null) {
            return productRepository.findProductByNameContainsAndPriceBetween(name, min, max, pageable);
        }

        return productPages;
    }

    @Override
    public List<Product> findProductInStock() {
        return productRepository.findProductInStock();
    }

    @Override
    public Page<Product> findByCategoryName(int pageNum, String keyword) {

        Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_CATEGORY_PER_PAGE);

        return productRepository.findByCategoryName(keyword, pageable);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductContainsPromotion> findAllProductContainsPromotion() {
        return productRepository.findAllProductContainsPromotion();
    }

    @Override
    public Product saveProduct(Product product) {
        boolean isUpdating = product.getId() != null;
        LocalDateTime now = LocalDateTime.now();

        if (isUpdating) {
            Product productInDb = findById(product.getId());
            product.setCreatedDate(productInDb.getCreatedDate());
            product.setUpdatedDate(now);
        } else {
            product.setCreatedDate(now);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateQtyProduct(int id, int qty, int sold) {
        productRepository.updateQtyProduct(id, qty, sold);
    }

    @Override
    public void updateDiscountPriceInProduct(Integer id, Integer promotionId, BigDecimal discountPrice) {
        productRepository.updateDiscountPriceInProduct(id, promotionId, discountPrice);
    }
}
