package com.assignment;

import com.assignment.dto.ProductContainsPromotion;
import com.assignment.entity.Product;
import com.assignment.entity.ProductCategory;
import com.assignment.entity.Review;
import com.assignment.product.service.IProductCateService;
import com.assignment.product.service.IProductService;
import com.assignment.promotion.repo.PromotionRepository;
import com.assignment.review.dto.ReviewRequest;
import com.assignment.review.service.IReviewService;
import com.assignment.security.service.UserDetail;
import com.assignment.sell.service.IInvoiceDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private HttpSession session;

    private IProductService productService;

    private IProductCateService productCateService;

    private IReviewService reviewService;

    private IInvoiceDetailService invoiceDetailService;

    @Autowired
    public MainController(IProductService productService,
                          IProductCateService productCateService,
                          IReviewService reviewService,
                          IInvoiceDetailService invoiceDetailService) {
        this.productService = productService;
        this.productCateService = productCateService;
        this.reviewService = reviewService;
        this.invoiceDetailService = invoiceDetailService;
    }

    @GetMapping("/home")
    public String viewHome() {
        return "home";
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/cat")
    public String viewHomePage(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                               Model model) {

        List<ProductContainsPromotion> productContainsPromotions = productService.findAllProductContainsPromotion();
        LocalDateTime toDay = LocalDateTime.now();

        // Tự động hủy khuyến mãi của sản phẩm khi ngày kết thúc < hôm nay
        productContainsPromotions.forEach(p -> {
            int compareToDate = toDay.compareTo(p.getEndDate());
            if(compareToDate > 0) {
                productService.updateDiscountPriceInProduct(p.getProductId(), null, null);
            }
        });

        List<ProductCategory> productCategories = productCateService.findAllProductCate();

        Page<Product> productPage = productService.findByPage(pageNum, keyword);
        List<Product> products = productPage.getContent();

        int totalPage = productPage.getTotalPages();
        int currentPage = productPage.getNumber();
        long totalItems = productPage.getTotalElements();

        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPage);

        if (startPage + 1 == totalPage && startPage > 1) {
            startPage -= 1;
        }

        model.addAttribute("productCategories", productCategories);

        model.addAttribute("products", products);
//        model.addAttribute("userId", "User Id: " + UserDetail.getId());

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/sell/detail-product/{id}")
    public String viewDetailProduct(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {

        int countBuyProduct = 0;
        int countReviewProduct = 0;
        if(request.getRemoteUser() != null) {
            countBuyProduct = invoiceDetailService.countBuyProduct(UserDetail.getId(), id);
            countReviewProduct = reviewService.countReviewProduct(UserDetail.getId(), id);
        }

        List<Review> reviews = reviewService.findReviewByProduct(id);
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("reviewRequest", new ReviewRequest());
        model.addAttribute("reviews", reviews);
        model.addAttribute("productId", id);
//        model.addAttribute("userId", UserDetail.getId());
        model.addAttribute("countBuyProduct", countBuyProduct);
        model.addAttribute("countReviewProduct", countReviewProduct);
        return "sell/product-detail";
    }

    @GetMapping("/sell/buy-now/{id}")
    public String viewBuyNow(@PathVariable("id") Integer id, Model model) {

        session.setAttribute("idProduct", id);
        Product product = productService.findById(id);

        model.addAttribute("product", product);
        return "sell/payment";
    }
}
