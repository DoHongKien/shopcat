package com.assignment.sell.controller;

import com.assignment.dto.ProductInfo;
import com.assignment.entity.Invoice;
import com.assignment.entity.InvoiceDetail;
import com.assignment.entity.Product;
import com.assignment.entity.User;
import com.assignment.product.service.IProductService;
import com.assignment.security.service.UserDetail;
import com.assignment.sell.service.IInvoiceDetailService;
import com.assignment.sell.service.IInvoiceService;
import com.assignment.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;

@Controller
public class BuyNowController {

    @Autowired
    private HttpSession session;

    private UserService userService;

    private IProductService productService;

    private IInvoiceService invoiceService;

    private IInvoiceDetailService invoiceDetailService;

    @Autowired
    public BuyNowController(IInvoiceService invoiceService,
                            IInvoiceDetailService invoiceDetailService,
                            IProductService productService,
                            UserService userService) {
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/buy-now/view-payment")
    public String viewPayment(@ModelAttribute("productInfo") ProductInfo productInfo,
                              @RequestParam("quantity") Integer quantity,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        Product product = productService.findById(productInfo.getId());

        if (productInfo.getQty() > quantity) {

            model.addAttribute("product", product);
            redirectAttributes.addFlashAttribute("greaterThanQty", "Bạn đã chọn quá số lượng sản phẩm còn");
            return "sell/product-detail";
        } else {
            BigDecimal priceProduct;
            if(product.getDiscountPrice() == null) {
                priceProduct = product.getPrice();
            } else {
                priceProduct = product.getDiscountPrice();
            }

            ProductInfo pInfo = ProductInfo
                    .builder()
                    .id(product.getId())
                    .name(product.getName())
                    .qty(productInfo.getQty())
                    .price(priceProduct)
                    .totalAmount(product.getPrice().multiply(BigDecimal.valueOf(productInfo.getQty())))
                    .build();

            session.setAttribute("productInfo", pInfo);

            model.addAttribute("product", pInfo);
            return "sell/payment";
        }

    }

    @GetMapping("/buy-now/payment")
    public String payment(RedirectAttributes redirectAttributes) {

        int userId = UserDetail.getId();
        User user = userService.findById(userId);
        ProductInfo productInfo = (ProductInfo) session.getAttribute("productInfo");
        Product product = productService.findById(productInfo.getId());

        Invoice invoice = new Invoice(user, productInfo.getTotalAmount(), new Date(), true);
        InvoiceDetail invoiceDetail = new InvoiceDetail(product, productInfo.getQty(), productInfo.getPrice(), true);
        invoice.addInvoiceDetail(invoiceDetail);

        invoiceService.saveInvoice(invoice);
        invoiceDetailService.saveInvoiceDetail(invoiceDetail);

        productService.updateQtyProduct(productInfo.getId(),
                product.getQuantity() - productInfo.getQty(),
                product.getSold() + productInfo.getQty());

        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được thanh toán");
        session.removeAttribute("productInfo");

        return "sell/thank-you";
    }
}
