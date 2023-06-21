package com.assignment.promotion.controller;

import com.assignment.entity.Product;
import com.assignment.entity.Promotion;
import com.assignment.product.service.IProductService;
import com.assignment.promotion.service.IPromotionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/promotion")
public class SuperSaleController {

    @Autowired
    private HttpSession session;

    private IPromotionService promotionService;

    private IProductService productService;

    @Autowired
    public SuperSaleController(IPromotionService promotionService, IProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @GetMapping("/show")
    public String viewPromotion(Model model) {

        List<Promotion> promotions = promotionService.findAllPromotion();
        model.addAttribute("promotions", promotions);

        List<Product> productListInventory = productService.findProductInStock();
        model.addAttribute("productListInventory", productListInventory);
        return "promotion/home-promotion";
    }

    @GetMapping("/view-add")
    public String viewAddPromotion(Model model) {

        model.addAttribute("promotion", new Promotion());
        return "promotion/add-promotion";
    }

    @PostMapping("/add")
    public String addPromotion(@ModelAttribute Promotion promotion, RedirectAttributes redirectAttributes) {

        promotionService.savePromotion(promotion);
        redirectAttributes.addFlashAttribute("message", "Lưu khuyến mãi thành công");

        return "redirect:/promotion/show";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdatePromotion(@PathVariable("id") Integer id, Model model) {

        Promotion promotion = promotionService.findById(id);
        model.addAttribute("promotion", promotion);
        return "promotion/update-promotion";
    }

    @GetMapping("/delete/{id}")
    public String deletePromotion(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        promotionService.deletePromotion(id);
        redirectAttributes.addFlashAttribute("message", "Xóa khuyến mãi thành công");
        return "redirect:/promotion/show";
    }

//    @GetMapping("/apply/{id}")
//    public String applyPromotion(@PathVariable("id") Integer id,
//                                 @RequestParam("productName") String productName,
//                                 RedirectAttributes redirectAttributes) {
//
//        Product productInDB = productService.findById(Integer.parseInt(productName));
//        Promotion promotionInDB = promotionService.findById(id);
//
//        BigDecimal afterApplyDiscount = productInDB.getPrice()
//                .multiply(BigDecimal.valueOf(promotionInDB.getDiscountPercentage()).divide(BigDecimal.valueOf(100)));
//
//        BigDecimal discountPrice = productInDB.getPrice().subtract(afterApplyDiscount);
//
//        promotionService.updateProductInPromotion(promotionInDB.getId(), productInDB.getId());
//        productService.updateDiscountPriceInProduct(productInDB.getId(), discountPrice);
//
//
//        redirectAttributes.addFlashAttribute("message", "Xóa khuyến mãi thành công");
//        return "redirect:/promotion/show";
//    }

    @GetMapping("/sale/{id}")
    public String saleProduct(@PathVariable("id") Integer id, Model model) {

        List<Promotion> promotions = promotionService.findAllPromotion();
        model.addAttribute("promotions", promotions);

        session.setAttribute("productId", id);
        return "promotion/sale-product";
    }

    @GetMapping("/apply/{id}")
    public String applyPromotionToProduct(@PathVariable("id") Integer promotionId) {

        Integer idProduct = (Integer) session.getAttribute("productId");

        Product productInDB = productService.findById(idProduct);
        Promotion promotionInDB = promotionService.findById(promotionId);

        BigDecimal afterApplyDiscount = productInDB.getPrice()
                .multiply(BigDecimal.valueOf(promotionInDB.getDiscountPercentage()).divide(BigDecimal.valueOf(100)));

        BigDecimal discountPrice = productInDB.getPrice().subtract(afterApplyDiscount);

        promotionService.updateProductInPromotion(promotionInDB.getId(), productInDB.getId());
        productService.updateDiscountPriceInProduct(productInDB.getId(), discountPrice);

        session.removeAttribute("productId");
        return "redirect:/cat";
    }
}
