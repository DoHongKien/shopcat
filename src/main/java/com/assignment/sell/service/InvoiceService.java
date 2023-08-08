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
public class InvoiceService implements IInvoiceService {

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
    public List<InvoiceDto> findInvoiceDetailByInvoice(Integer invoiceId) {
        return invoiceRepository.findInvoiceDetailByInvoice(invoiceId);
    }

    @Override
    public Page<InvoiceDto> findAllInvoiceUser(Pageable pageable) {
        return invoiceRepository.findAllInvoiceUser(pageable);
    }

    @Override
    public List<Invoice> findAllWithProduct() {
        return invoiceRepository.findAllWithProduct();
    }

    @Override
    public List<Invoice> findAllWithProduct(Integer userId) {
        return invoiceRepository.findAllWithProduct(userId);
    }

    @Override
    public List<Invoice> findAllWithProductByStatus(String status) {
        return invoiceRepository.findAllWithProductByStatus(status);
    }

    @Override
    public List<Invoice> findAllWithProductByStatus(Integer userId, String status) {
        return invoiceRepository.findAllWithProductByStatus(userId, status);
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Integer id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public int updateStatusInvoice(Integer invoiceId, String status) {
        return switch (status) {
            case "Đang giao hàng" -> invoiceRepository.updateStatusInvoice(invoiceId, "Đang giao hàng");
            case "Đã hoàn thành" -> invoiceRepository.updateStatusInvoice(invoiceId, "Đã hoàn thành");
            case "Đã hủy" -> invoiceRepository.updateStatusInvoice(invoiceId, "Đã hủy");
            default -> 0;
        };
    }
}
