package com.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductInCart implements Serializable {

    private Integer id;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
