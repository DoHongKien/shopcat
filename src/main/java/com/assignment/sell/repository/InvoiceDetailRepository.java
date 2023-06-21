package com.assignment.sell.repository;

import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {

    @Query("""
            SELECT new com.assignment.dto.InvoiceStatisticDto(p.id, p.name, SUM(id.quantity))
            FROM InvoiceDetail id JOIN id.product p JOIN id.invoice i
            WHERE i.paymentDate = :day
            GROUP BY p.id, p.name
            ORDER BY SUM(id.quantity) DESC
            """)
    List<InvoiceStatisticDto> findTopSellingProductsByPaymentDate(@Param("day") Date paymentDate);

    @Query("""
            SELECT new com.assignment.dto.InvoiceStatisticDto(p.id, p.name, SUM(id.quantity))
            FROM InvoiceDetail id JOIN id.product p JOIN id.invoice i 
            WHERE i.paymentDate >= :week
            GROUP BY p.id, p.name 
            ORDER BY SUM(id.quantity) DESC
            """)
    List<InvoiceStatisticDto> findTopSellingProducts(@Param("week") Date paymentDate);

    @Query("""
            SELECT new com.assignment.dto.InvoiceStatisticDto(p.id, p.name, SUM(id.quantity))
            FROM InvoiceDetail id
            JOIN Product p ON id.product.id = p.id
            JOIN Invoice i ON id.invoice.id = i.id
            WHERE FUNCTION('MONTH', i.paymentDate) = :month AND FUNCTION('YEAR', i.paymentDate) = :year
            GROUP BY p.id, p.name
            ORDER BY SUM(id.quantity) DESC
            """)
    List<InvoiceStatisticDto> findTopSelling(@Param("month") Integer month, @Param("year") Integer year);
}
