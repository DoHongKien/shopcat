package com.assignment.review.service;

import com.assignment.entity.Review;

import java.util.List;

public interface IReviewService {

    List<Review> findReviewByProduct(Integer id);

    Review findById(Integer id);

    int countReviewProduct(Integer userId, Integer productId);

    Review saveReview(Review review);

    void deleteReview(Integer id);
}
