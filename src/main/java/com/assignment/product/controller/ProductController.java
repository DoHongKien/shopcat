package com.assignment.product.controller;

import com.assignment.dto.Country;
import com.assignment.entity.Product;
import com.assignment.product.service.CountryService;
import com.assignment.product.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private IProductService productService;

    private CountryService countryService;

    @Autowired
    public ProductController(IProductService productService, CountryService countryService) {
        this.productService = productService;
        this.countryService = countryService;
    }

    @GetMapping
    public String viewHome(Model model) {
        return viewHomePage(model, 1, null, null, null);
    }

    @GetMapping("/page/{pageNum}")
    public String viewHomePage(Model model,
                               @PathVariable("pageNum") int pageNum,
                               @Param("keyword") String keyword,
                               @Param("min") BigDecimal min,
                               @Param("max") BigDecimal max) {

        List<Country> countries = countryService.getAllCountry();

        List<Product> products = null;
        Page<Product> productKeyword = productService.findByPage(pageNum, keyword);
        Page<Product> productPrice = productService.findProductByPriceBetween(pageNum, min, max);
        Page<Product> productKeywordAndPrice = productService.findProductByNameContainsAndPriceBetween(pageNum, keyword, min, max);

        int totalPage = 0;
        int currentPage = 0;
        long totalItems = 0;

        if (keyword == null && (min == null && max == null)) {
            products = productKeyword.getContent();
            totalPage = productKeyword.getTotalPages();
            currentPage = productKeyword.getNumber() + 1;

            totalItems = productKeyword.getTotalElements();
        } else if (keyword != null && (min == null && max == null)) {
            products = productKeyword.getContent();
            totalPage = productKeyword.getTotalPages();
            currentPage = productKeyword.getNumber() + 1;

            totalItems = productKeyword.getTotalElements();
        } else if (keyword == null && (min != null && max != null)) {
            products = productPrice.getContent();
            totalPage = productPrice.getTotalPages();
            currentPage = productPrice.getNumber() + 1;

            totalItems = productPrice.getTotalElements();
        } else if (keyword != null && (min != null && max != null)) {
            products = productKeywordAndPrice.getContent();
            totalPage = productKeywordAndPrice.getTotalPages();
            currentPage = productKeywordAndPrice.getNumber() + 1;

            totalItems = productKeywordAndPrice.getTotalElements();
        }


        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPage);

        if (startPage + 1 == totalPage && startPage > 1) {
            startPage -= 1;
        }

        model.addAttribute("listProduct", products);
        model.addAttribute("countries", countries);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("keyword", keyword);
        model.addAttribute("min", min);
        model.addAttribute("max", max);

        return "products/home-product";
    }

    @GetMapping("/detail/{id}")
    public String detailProduct(@PathVariable int id, Model model) {

        Product product = productService.findById(id);
        List<Country> countries = countryService.getAllCountry();
        model.addAttribute("countries", countries);
        model.addAttribute("product", product);
        return "products/update-product";
    }

    @GetMapping("/viewadd")
    public String viewAdd(Model model) {

        List<Country> countries = countryService.getAllCountry();
        model.addAttribute("countries", countries);
        model.addAttribute("product", new Product());
        return "products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (result.hasErrors()) {
            List<Country> countries = countryService.getAllCountry();
            model.addAttribute("countries", countries);
            return "products/add-product";
        }

        productService.saveProduct(product);

        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được lưu thành công");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Sản phẩm có id là " + id + " đã được xóa");
        return "redirect:/products";
    }
}
