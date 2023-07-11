package com.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceDto {

    private Integer invoiceId;
    private String userName;
    private Long totalProduct;
    private BigDecimal totalAmount;
    private Date paymentDate;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private Boolean status;

    public InvoiceDto(Integer invoiceId, String userName, Long totalProduct, BigDecimal totalAmount, Date paymentDate) {
        this.invoiceId = invoiceId;
        this.userName = userName;
        this.totalProduct = totalProduct;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
    }

    public InvoiceDto(String userName, String productName, Integer quantity, BigDecimal price, Boolean status) {
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }
}
