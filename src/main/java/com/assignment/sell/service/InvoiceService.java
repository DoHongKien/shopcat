package com.assignment.sell.service;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import com.assignment.sell.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService implements IInvoiceService{

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<Invoice> findAllInvoice() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice findByIdInvoice(Integer id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public Page<InvoiceDto> findAllInvoiceUser(Pageable pageable) {
        return invoiceRepository.findAllInvoiceUser(pageable);
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Integer id) {
        invoiceRepository.deleteById(id);
    }
}
