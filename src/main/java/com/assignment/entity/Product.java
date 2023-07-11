package com.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sold")
    private Integer sold;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "origin")
    private String origin;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "lifespan")
    private Integer lifespan;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    public Product(Integer id, String name, String image, Integer quantity, BigDecimal price, String origin,
                   Integer lifespan, String description, Boolean status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.origin = origin;
        this.lifespan = lifespan;
        this.description = description;
        this.status = status;
    }

    @Transient
    public String getPathImage() {
        if(id == null || image == null) return "/images/image-thumbnail.png";
        return "/product-images/" + this.id + "/" + this.image;
    }
}
