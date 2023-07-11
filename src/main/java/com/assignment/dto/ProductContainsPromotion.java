package com.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductContainsPromotion {

    private Integer productId;
    private Integer promotionId;
    private LocalDateTime endDate;
}
