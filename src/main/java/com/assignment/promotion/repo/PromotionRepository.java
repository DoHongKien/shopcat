package com.assignment.promotion.repo;

import com.assignment.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

//    @Transactional
//    @Modifying
//    @Query("UPDATE Promotion p SET p.product.id = :productId WHERE p.id = :id")
//    void updateProductInPromotion(@Param("id") Integer id, @Param("productId") Integer productId);
}
