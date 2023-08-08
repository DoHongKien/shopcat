package com.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InvoiceDetail> invoiceDetails;

    public Invoice(User user, BigDecimal totalAmount, Date paymentDate, String status) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetails.add(invoiceDetail);
        invoiceDetail.setInvoice(this);
    }

    public void removeInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetails.remove(invoiceDetail);
        invoiceDetail.setInvoice(null);
    }
}
