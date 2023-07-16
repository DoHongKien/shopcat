package com.assignment.product.controller;

import com.assignment.FileUploadUtil;
import com.assignment.dto.Country;
import com.assignment.entity.Product;
import com.assignment.entity.ProductCategory;
import com.assignment.product.export.ProductCsvExporter;
import com.assignment.product.export.ProductExcelExporter;
import com.assignment.product.export.ProductPdfExporter;
import com.assignment.product.service.CountryService;
import com.assignment.product.service.IProductCateService;
import com.assignment.product.service.IProductService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/products")
public class ProductController {

    private IProductService productService;

    private IProductCateService productCateService;

    private CountryService countryService;

    @Autowired
    public ProductController(IProductService productService,
                             IProductCateService productCateService,
                             CountryService countryService) {
        this.productService = productService;
        this.productCateService = productCateService;

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

    @GetMapping("/filter/categories/{category}")
    public String filterCategories(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @PathVariable(value = "category", required = false) String category,
                                   Model model) {

        List<ProductCategory> productCategories = productCateService.findAllPrductCate();

        Page<Product> products = productService.findByCategoryName(pageNum, category);

        int totalPage = products.getTotalPages();
        int currentPage = products.getNumber();
        long totalItems = products.getTotalElements();

        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPage);

        if (startPage + 1 == totalPage && startPage > 1) {
            startPage -= 1;
        }

        model.addAttribute("productCategories", productCategories);
        model.addAttribute("productLists", products.getContent());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("category", category);
        return "home-shop";
    }

    @GetMapping("/detail/{id}")
    public String detailProduct(@PathVariable int id, Model model) {

        Product product = productService.findById(id);
        List<Country> countries = countryService.getAllCountry();
        List<ProductCategory> productCategories = productCateService.findAllPrductCate();

        model.addAttribute("countries", countries);
        model.addAttribute("productCategories", productCategories);
        model.addAttribute("product", product);
        return "products/update-product";
    }

    @GetMapping("/viewadd")
    public String viewAdd(Model model) {

        List<Country> countries = countryService.getAllCountry();
        List<ProductCategory> productCategories = productCateService.findAllPrductCate();

        model.addAttribute("countries", countries);
        model.addAttribute("productCategories", productCategories);
        model.addAttribute("product", new Product());
        return "products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             @RequestParam("imageProduct") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes,
                             Model model) throws IOException {


        if (result.hasErrors()) {
            List<Country> countries = countryService.getAllCountry();
            model.addAttribute("countries", countries);
            return "products/add-product";
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            product.setImage(fileName);
            Product saveProduct = productService.saveProduct(product);

            String uploadDir = "product-images/" + saveProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (product.getImage().isEmpty()) product.setImage(null);
            productService.saveProduct(product);
        }

        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được lưu thành công");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {

        productService.deleteProduct(id);
        String userImageDir = "product-images/" + id;
        FileUploadUtil.removeFile(userImageDir);

        redirectAttributes.addFlashAttribute("message", "Sản phẩm có id là " + id + " đã được xóa");
        return "redirect:/products";
    }

    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<Product> products = productService.findAll();
        ProductCsvExporter csvExporter = new ProductCsvExporter();
        csvExporter.export(response, products);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Product> products = productService.findAll();
        ProductExcelExporter excelExporter = new ProductExcelExporter();
        excelExporter.export(response, products);
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException, DocumentException {
        List<Product> products = productService.findAll();
        ProductPdfExporter pdfExporter = new ProductPdfExporter();
        pdfExporter.export(response, products);
    }
}
