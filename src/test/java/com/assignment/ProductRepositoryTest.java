package com.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import com.assignment.entity.Product;
import com.assignment.product.repository.IProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private IProductRepository productRepository;

    @Test
    public void findAll() {
        List<Product> listProduct = productRepository.findAll();
        listProduct.forEach(p -> System.out.println(p));
    }

    @Test
    public void findById() {
        Product product = productRepository.findById(1).get();
        assertThat(product).isNotNull();
    }

//    @Test
//    public void findTop5ProductByCreatedDateAsc() {
//        List<Product> products = productRepository.findFirst10ByOrderByCreatedDateAsc();
//        for(Product p: products) {
//            System.out.print(p.getId() + " | ");
//        }
//        assertThat(products.size()).isGreaterThan(0);
//    }

//    @Test
//    public void findProductByPriceBetween() {
//        Pageable pageable = PageRequest.of(0, 2);
//        List<Product> listProduct =
//                productRepository.findProductByPriceBetween(BigDecimal.valueOf(2000000), BigDecimal.valueOf(4000000), pageable);
//        System.out.println(listProduct);
//        assertThat(listProduct.size()).isGreaterThan(0);
//    }
//
//    @Test
//    public void findProductByNameAndPriceBetween() {
//        Pageable pageable = PageRequest.of(0, 1);
//        List<Product> listProduct =
//                productRepository.findProductByNameContainsAndPriceBetween("selkirk", BigDecimal.valueOf(2000000), BigDecimal.valueOf(4000000), pageable);
//        System.out.println(listProduct);
//        assertThat(listProduct.size()).isGreaterThan(0);
//    }

    @Test
    @Transactional
    @Rollback
    public void saveProduct() {
        Product p = new Product();
        p.setName("1");
        p.setCategory("1");
        p.setDescription("1");
        p.setPrice(BigDecimal.valueOf(100));
        p.setOrigin("1");
        p.setQuantity(1);
        p.setStatus(true);
        Product product = productRepository.save(p);
        assertThat(product.getId()).isGreaterThan(0);
    }

    @Test
    @Transactional
    @Rollback
    public void updateProduct() {
        Product product = productRepository.findById(1).get();
        product.setQuantity(23);
        productRepository.save(product);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteProduct() {
        Integer product = 1;
        productRepository.deleteById(product);
    }
}
