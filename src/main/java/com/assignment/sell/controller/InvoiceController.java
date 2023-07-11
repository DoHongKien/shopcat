package com.assignment.sell.controller;

import com.assignment.dto.InvoiceDto;
import com.assignment.sell.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InvoiceController {

    private IInvoiceService invoiceService;

    @Autowired
    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/show-invoice")
    public String showAllInvoice(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "itemOfPage", defaultValue = "5") Integer itemOfPage,
                                 Model model) {

        Sort sort = Sort.by("paymentDate").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, itemOfPage, sort);
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
        model.addAttribute("itemOfPage", itemOfPage);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "invoice/home-invoice";
    }

    @GetMapping("/detail-invoice/{id}")
    public ResponseEntity<List<InvoiceDto>> findInvoiceDetail(@PathVariable("id") String id) {

        int invoiceId = Integer.parseInt(id);
        List<InvoiceDto> invoicesDto = invoiceService.findInvoiceDetailByInvoice(invoiceId);

        return new ResponseEntity<>(invoicesDto, HttpStatus.OK);
    }
}
