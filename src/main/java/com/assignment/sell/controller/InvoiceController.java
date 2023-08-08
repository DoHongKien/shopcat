package com.assignment.sell.controller;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Invoice;
import com.assignment.security.service.UserDetail;
import com.assignment.sell.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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

    @GetMapping("/invoice/info-invoice")
    public String viewInfoInvoice(Model model,
                                  RedirectAttributes redirectAttributes) {

        List<Invoice> invoicesByAll = null;
        List<Invoice> invoicesByPending = null;
        List<Invoice> invoicesByShipping = null;
        List<Invoice> invoicesBySuccess = null;
        List<Invoice> invoicesByCanceled = null;

        List<String> roles = UserDetail.getRoles();
        int userId = UserDetail.getId();

        for (String role : roles) {
            if (role.equals("ADMIN")) {
                invoicesByAll = invoiceService.findAllWithProduct();
                invoicesByPending = invoiceService.findAllWithProductByStatus("Đang xử lý");
                invoicesByShipping = invoiceService.findAllWithProductByStatus("Đang giao hàng");
                invoicesBySuccess = invoiceService.findAllWithProductByStatus("Đã giao hàng");
                invoicesByCanceled = invoiceService.findAllWithProductByStatus("Đã hủy");
            } else if(role.equals("USER")) {
                invoicesByAll = invoiceService.findAllWithProduct(userId);
                invoicesByPending = invoiceService.findAllWithProductByStatus(userId, "Đang xử lý");
                invoicesByShipping = invoiceService.findAllWithProductByStatus(userId, "Đang giao hàng");
                invoicesBySuccess = invoiceService.findAllWithProductByStatus(userId, "Đã giao hàng");
                invoicesByCanceled = invoiceService.findAllWithProductByStatus(userId, "Đã hủy");
            }
        }


        model.addAttribute("invoicesByAll", invoicesByAll);
        model.addAttribute("invoicesByPending", invoicesByPending);
        model.addAttribute("invoicesByShipping", invoicesByShipping);
        model.addAttribute("invoicesBySuccess", invoicesBySuccess);
        model.addAttribute("invoicesByCanceled", invoicesByCanceled);

        if (redirectAttributes.getFlashAttributes().containsKey("message")) {
            int message = (int) redirectAttributes.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

        return "sell/status-payment";
    }

    @PostMapping("/invoice/status-change-invoice")
    public String updateStatusInvoice(@RequestParam("invoiceId") Integer invoiceId,
                                      @RequestParam("status") String status,
                                      RedirectAttributes redirectAttributes) {

        int result = invoiceService.updateStatusInvoice(invoiceId, status);
        if (result > 0) {
            redirectAttributes.addFlashAttribute("message",
                    "Chuyển trạng thái thành công");
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Chuyển trạng thái thất bại do có lỗi xảy ra");
        }

        return "redirect:/invoice/info-invoice";
    }
}
