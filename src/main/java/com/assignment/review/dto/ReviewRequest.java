package com.assignment.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewRequest {

    private Integer reviewId;

    private Integer productId;

    private Integer rating;

    private String title;

    private String content;

    private Boolean approved;

    private Date createdAt;

    private Date updatedAt;
}
