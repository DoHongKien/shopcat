package com.assignment.sell.repository;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import com.assignment.entity.InvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("""
            SELECT new com.assignment.dto.InvoiceDto(i.id, u.name, COUNT(id.invoice.id), i.totalAmount, i.paymentDate, i.status) 
            FROM Invoice i JOIN InvoiceDetail id ON i.id = id.invoice.id JOIN User u ON i.user.id = u.id
            GROUP BY i.id, u.name, i.totalAmount, i.paymentDate, i.status
            """)
    Page<InvoiceDto> findAllInvoiceUser(Pageable pageable);

    @Query("""
            SELECT new com.assignment.dto.InvoiceDto(u.name, p.name, id.quantity, id.price) 
            FROM Invoice i JOIN InvoiceDetail id ON i.id = id.invoice.id JOIN User u ON i.user.id = u.id
            JOIN Product p ON id.product.id = p.id
            WHERE i.id = :invoiceId
            """)
    List<InvoiceDto> findInvoiceDetailByInvoice(@Param("invoiceId") Integer invoiceId);

    @Query("SELECT i FROM Invoice i JOIN FETCH i.invoiceDetails d JOIN FETCH d.product ORDER BY i.paymentDate desc")
    List<Invoice> findAllWithProduct();

    @Query("""
            SELECT i FROM Invoice i JOIN FETCH i.invoiceDetails d JOIN FETCH d.product 
            WHERE i.user.id = ?1
            ORDER BY i.paymentDate desc
            """)
    List<Invoice> findAllWithProduct(Integer userId);

    @Query("""
            SELECT i FROM Invoice i JOIN FETCH i.invoiceDetails d JOIN FETCH d.product 
            WHERE i.status = ?1 
            ORDER BY i.paymentDate DESC
            """)
    List<Invoice> findAllWithProductByStatus(String status);

    @Query("""
            SELECT i FROM Invoice i JOIN FETCH i.invoiceDetails d JOIN FETCH d.product 
            WHERE i.user.id = ?1 and i.status = ?2
            ORDER BY i.paymentDate DESC
            """)
    List<Invoice> findAllWithProductByStatus(Integer userId, String status);

    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.status = ?2 WHERE i.id = ?1")
    int updateStatusInvoice(Integer invoiceId, String status);
}
