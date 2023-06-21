package com.assignment;

import com.assignment.entity.Product;
import com.assignment.product.service.IProductService;
import com.assignment.security.UserDetail;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private HttpSession session;

    private IProductService productService;

    @Autowired
    public MainController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/cat")
    public String viewHomePage(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                               Model model) {

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

        model.addAttribute("products",products);
        model.addAttribute("userId","User Id: " + UserDetail.getId());

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @GetMapping("/sell/detail-product/{id}")
    public String viewDetailProduct(@PathVariable("id") Integer id, Model model) {

        Product product = productService.findById(id);
        model.addAttribute("product", product);
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
