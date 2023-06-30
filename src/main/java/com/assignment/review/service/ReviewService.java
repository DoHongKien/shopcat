package com.assignment.review.service;

import com.assignment.entity.Review;
import com.assignment.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> findReviewByProduct(Integer id) {

        return reviewRepository.findReviewByProduct(id);
    }

    @Override
    public Review findById(Integer id) {
        return reviewRepository.findById(id).get();
    }

    @Override
    public int countReviewProduct(Integer userId, Integer productId) {
        return reviewRepository.countReviewProduct(userId, productId);
    }

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
}
