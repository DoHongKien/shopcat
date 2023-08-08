package com.assignment.product.controller;

import com.assignment.entity.ProductCategory;
import com.assignment.product.service.IProductCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product-cate")
public class ProductCateController {

    @Autowired
    private IProductCateService service;

    @GetMapping
    public String showProductCate(Model model) {

        List<ProductCategory> productCategories = service.findAllProductCate();

        model.addAttribute("productCategory", new ProductCategory());
        model.addAttribute("productCategories", productCategories);

        return "product-category/home-product-cate";
    }

    @GetMapping("/detail/{id}")
    public String detailProductCate(@PathVariable("id") Integer id, Model model){

        List<ProductCategory> productCategories = service.findAllProductCate();
        ProductCategory productCategory = service.findById(id);

        model.addAttribute("productCategories", productCategories);
        model.addAttribute("productCategory", productCategory);
        return "product-category/home-product-cate";
    }

    @PostMapping("/add")
    public String addProductCate(@ModelAttribute ProductCategory productCategory, RedirectAttributes redirectAttributes) {

        service.saveProductCate(productCategory);

        redirectAttributes.addFlashAttribute("message", "Thể loại sản phẩm đã lưu thành công");
        return "redirect:/product-cate";
    }

    @GetMapping("/delete/{id}")
    public String deleteProductCate(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        service.deleteProductCate(id);

        redirectAttributes.addFlashAttribute("message", "Thể loại sản phẩm đã xóa thành công");
        return "redirect:/product-cate";
    }
}
