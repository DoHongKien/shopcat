package com.assignment.review.repository;

import com.assignment.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.product.id = ?1")
    List<Review> findReviewByProduct(Integer productId);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.user.id = ?1 AND r.product.id = ?2")
    int countReviewProduct(Integer userId, Integer productId);
}
