package com.assignment.dto;

import com.assignment.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductInCart implements Serializable {

    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private Product productIC;
}
