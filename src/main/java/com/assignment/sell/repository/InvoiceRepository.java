package com.assignment.sell.repository;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("""
            SELECT new com.assignment.dto.InvoiceDto(u.name, p.name, id.quantity, id.price, i.totalAmount, i.paymentDate) 
            FROM Invoice i JOIN InvoiceDetail id ON i.id = id.invoice.id JOIN Product p ON id.product.id = p.id 
            JOIN User u ON i.user.id = u.id
            """)
    Page<InvoiceDto> findAllInvoiceUser(Pageable pageable);
}
