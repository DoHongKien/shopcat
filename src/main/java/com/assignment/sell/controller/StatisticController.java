package com.assignment.sell.controller;

import com.assignment.dto.InvoiceStatisticDto;
import com.assignment.entity.Product;
import com.assignment.entity.Promotion;
import com.assignment.product.service.IProductService;
import com.assignment.promotion.service.IPromotionService;
import com.assignment.sell.service.IInvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class StatisticController {

    private IProductService productService;

    private IInvoiceDetailService invoiceDetailService;

    private IPromotionService promotionService;

    @Autowired
    public StatisticController(IProductService productService,
                               IInvoiceDetailService invoiceDetailService,
                               IPromotionService promotionService) {
        this.productService = productService;
        this.invoiceDetailService = invoiceDetailService;
        this.promotionService = promotionService;
    }

    @GetMapping("/statistics")
    public String viewStatistics(@RequestParam(value = "month", required = false) Integer month,
                                 @RequestParam(value = "week", required = false) String week,
                                 Model model) throws ParseException {

        Integer[] monthArray = {1,2,3,4,5,6,7,8,9,10,11,12};
        List<Integer> months = Arrays.asList(monthArray);

        List<Product> productListInventory = productService.findProductInStock();
        List<InvoiceStatisticDto> productTopSellingMonth = invoiceDetailService.findProductTopSellingMonth(month);
        List<InvoiceStatisticDto> productTopSellingWeek = invoiceDetailService.findProductTopSellingWeek(week);

        model.addAttribute("productListInventory", productListInventory);
        model.addAttribute("productTopSellingWeek", productTopSellingWeek);
        model.addAttribute("productTopSellingMonth", productTopSellingMonth);
        model.addAttribute("months", months);
        return "statistics/home-statistic";
    }
}
