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

    Invoice saveInvoice(Invoice invoice);

    void deleteInvoice(Integer id);
}
