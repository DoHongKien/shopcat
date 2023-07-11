package com.assignment.sell.repository;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("""
            SELECT new com.assignment.dto.InvoiceDto(i.id, u.name, COUNT(id.invoice.id), i.totalAmount, i.paymentDate) 
            FROM Invoice i JOIN InvoiceDetail id ON i.id = id.invoice.id JOIN User u ON i.user.id = u.id
            GROUP BY i.id, u.name, i.totalAmount, i.paymentDate
            """)
    Page<InvoiceDto> findAllInvoiceUser(Pageable pageable);

    @Query("""
            SELECT new com.assignment.dto.InvoiceDto(u.name, p.name, id.quantity, id.price, id.status) 
            FROM Invoice i JOIN InvoiceDetail id ON i.id = id.invoice.id JOIN User u ON i.user.id = u.id
            JOIN Product p ON id.product.id = p.id
            WHERE i.id = :invoiceId
            """)
    List<InvoiceDto> findInvoiceDetailByInvoice(@Param("invoiceId") Integer invoiceId);
}
