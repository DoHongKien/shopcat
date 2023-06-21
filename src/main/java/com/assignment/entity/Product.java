package com.assignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Tên không được để trống.")
    @Column(name = "name")
    private String name;

    @Min(value = 1, message = "Số lượng không được nhỏ hơn 1")
    @Max(value = 100000, message = "Số lượng không được lớn hơn 100000")
    @Column(name = "quantity")
    private Integer quantity;

    @DecimalMin(value = "1000", message = "Giá phải lớn hơn 1000")
    @Column(name = "price")
    private BigDecimal price;

    @NotEmpty(message = "Nguồn gốc không được để trống")
    @Column(name = "origin")
    private String origin;

    @NotEmpty(message = "Thể loại chưa được chọn")
    @Column(name = "category")
    private String category;

    @Min(value = 1, message = "Tuổi thọ trung bình không được nhỏ hơn 1")
    @Max(value = 100, message = "Tuổi thọ trung bình không được lớn hơn 100")
    @Column(name = "lifespan")
    private Integer lifespan;

    @NotEmpty(message = "Mô tả không được để trống.")
    @Column(name = "description")
    private String description;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    public Product(Integer id, String name, Integer quantity, BigDecimal price, String origin, String category,
                   Integer lifespan, String description, Boolean status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.origin = origin;
        this.category = category;
        this.lifespan = lifespan;
        this.description = description;
        this.status = status;
    }
}
