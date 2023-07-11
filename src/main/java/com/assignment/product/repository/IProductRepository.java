package com.assignment.product.repository;

import com.assignment.dto.ProductContainsPromotion;
import com.assignment.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


public interface IProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    Page<Product> findByKeyword(String keyword, Pageable pageable);

    Page<Product> findProductByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Product> findProductByNameContainsAndPriceBetween(String name, BigDecimal min, BigDecimal max, Pageable pageable);

    @Query("""
            SELECT p
            FROM Product p
            LEFT JOIN InvoiceDetail id ON p.id = id.product.id
            WHERE id.product.id IS NULL
            """)
    List<Product> findProductInStock();

    @Query("SELECT p FROM Product p JOIN FETCH ProductCategory pc ON p.category.id = pc.id WHERE pc.name LIKE %?1%")
    Page<Product> findByCategoryName(String category, Pageable pageable);

    @Query("""
            SELECT new com.assignment.dto.ProductContainsPromotion(p.id, pro.id, pro.endDate)
            FROM Product p JOIN Promotion pro ON p.promotion.id = pro.id
            """)
    List<ProductContainsPromotion> findAllProductContainsPromotion();

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.quantity = :quantity, p.sold = :sold WHERE p.id = :id")
    void updateQtyProduct(@Param("id") Integer id, @Param("quantity") Integer qty, @Param("sold") Integer sold);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.promotion.id = :promotionId, p.discountPrice = :discountPrice WHERE p.id = :id")
    void updateDiscountPriceInProduct(@Param("id") Integer id, @Param("promotionId") Integer promotionId, @Param("discountPrice") BigDecimal discountPrice);
}
