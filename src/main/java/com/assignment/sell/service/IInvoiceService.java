package com.assignment.sell.service;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInvoiceService {

    List<Invoice> findAllInvoice();

    Invoice findByIdInvoice(Integer id);

    Page<InvoiceDto> findAllInvoiceUser(Pageable pageable);

    List<InvoiceDto> findInvoiceDetailByInvoice(Integer invoiceId);

    List<Invoice> findAllWithProduct();

    List<Invoice> findAllWithProduct(Integer userId);

    List<Invoice> findAllWithProductByStatus(String status);

    List<Invoice> findAllWithProductByStatus(Integer userId, String status);

    Invoice saveInvoice(Invoice invoice);

    void deleteInvoice(Integer id);

    int updateStatusInvoice(Integer invoiceId, String status);
}
