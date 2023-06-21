package com.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductInfo {

    private Integer id;
    private String name;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal totalAmount;
}
