package com.assignment;

import com.assignment.entity.Review;
import com.assignment.review.repository.ReviewRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void findById() {

        Optional<Review> review = reviewRepository.findById(15);

        assertThat(review.isPresent()).isNotNull();
    }

    @Test
    public void countProduct() {

        int i = reviewRepository.countReviewProduct(1, 15);
        System.out.println(i);
        assertThat(i).isGreaterThan(0);
    }
}
