package com.assignment.sell.controller;

import com.assignment.dto.InvoiceDto;
import com.assignment.sell.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InvoiceController {

    private static final int INVOICE_PER_PAGE = 5;

    private IInvoiceService invoiceService;

    @Autowired
    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/show-invoice")
    public String showAllInvoice(@RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum, Model model) {

        Sort sort = Sort.by("paymentDate").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, INVOICE_PER_PAGE, sort);
        Page<InvoiceDto> invoiceDtos = invoiceService.findAllInvoiceUser(pageable);

        List<InvoiceDto> invoiceList = invoiceDtos.getContent();

        int totalPage = invoiceDtos.getTotalPages();
        int currentPage = invoiceDtos.getNumber() + 1;
        long totalItems = invoiceDtos.getTotalElements();

        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPage);

        if (startPage + 1 == totalPage && startPage > 1) {
            startPage -= 1;
        }

        model.addAttribute("invoiceList", invoiceList);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "invoice/home-invoice";
    }
}
