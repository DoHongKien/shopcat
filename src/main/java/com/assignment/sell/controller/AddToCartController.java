package com.assignment.sell.controller;

import com.assignment.dto.ProductInCart;
import com.assignment.dto.ProductInfo;
import com.assignment.entity.*;
import com.assignment.product.service.IProductService;
import com.assignment.security.UserDetail;
import com.assignment.sell.service.ICartDetailService;
import com.assignment.sell.service.ICartService;
import com.assignment.sell.service.IInvoiceDetailService;
import com.assignment.sell.service.IInvoiceService;
import com.assignment.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AddToCartController {

    @Autowired
    private HttpSession session;

    private UserService userService;

    private IProductService productService;

    private ICartService cartService;

    private ICartDetailService cartDetailService;

    private IInvoiceService invoiceService;

    private IInvoiceDetailService invoiceDetailService;

    @Autowired
    public AddToCartController(ICartService cartService,
                               ICartDetailService cartDetailService,
                               IProductService productService,
                               UserService userService,
                               IInvoiceService invoiceService,
                               IInvoiceDetailService invoiceDetailService) {
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.productService = productService;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
    }

    @GetMapping("/add-to-cart/view-cart")
    public String viewCart(Model model) {

        List<ProductInCart> listCartDetail = cartDetailService.findAllCartDetailByUser(UserDetail.getId());

        BigDecimal totalAmountCart = listCartDetail
                .stream()
                .map(l -> l.getPrice().multiply(BigDecimal.valueOf(l.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalAmountCart", totalAmountCart);
        model.addAttribute("listCartDetail", listCartDetail);
        model.addAttribute("checkEmptyCart", listCartDetail.size());
        return "sell/home-cart";
    }

    @PostMapping("/add-to-cart/add-product")
    public String addProductToCart(@ModelAttribute("productInfo") ProductInfo productInfo,
                                   Model model) {

        Product product = productService.findById(productInfo.getId());

        BigDecimal productPrice;

        if(product.getDiscountPrice() == null) {
            productPrice = product.getPrice().multiply(BigDecimal.valueOf(productInfo.getQty()));
        } else {
            productPrice = product.getDiscountPrice().multiply(BigDecimal.valueOf(productInfo.getQty()));
        }

        ProductInfo pInfo = ProductInfo
                .builder()
                .id(productInfo.getId())
                .name(productInfo.getName())
                .qty(productInfo.getQty())
                .totalAmount(productPrice)
                .build();

        CartDetail productInCart = cartDetailService.findByProductInCart(productInfo.getId(), cartService.findCartByUser(UserDetail.getId()));

        if (productInCart != null) {

            int productId = productInfo.getId();
            int cartDetailId = cartService.findCartByUser(UserDetail.getId());
            int quantity = pInfo.getQty() + productInCart.getQuantity();
            BigDecimal price = pInfo.getTotalAmount().add(productInCart.getPrice());
            cartDetailService.updateQuantityAndPriceInCart(productId, cartDetailId, quantity, price);

        } else {

            Cart cart = new Cart();
            cart.setId(cartService.findCartByUser(UserDetail.getId()));

            CartDetail cartDetail = new CartDetail();
            cartDetail.setProduct(product);
            cartDetail.setCart(cart);
            cartDetail.setQuantity(pInfo.getQty());
            cartDetail.setPrice(pInfo.getTotalAmount());
            cartDetail.setStatus(true);

            cartDetailService.saveCartDetail(cartDetail);
        }

        model.addAttribute("product", product);
        return "sell/product-detail";
    }

    @PostMapping("/add-to-cart/buy-now")
    public String cartPayment(@RequestParam(value = "selectProduct", required = false) Integer[] selectProducts,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        List<ProductInCart> productInCarts = new ArrayList<>();
        List<Product> products = productService.findAll();

        List<ProductInCart> listCartDetail = cartDetailService.findAllCartDetailByUser(UserDetail.getId());

        if(selectProducts == null) {
            redirectAttributes.addFlashAttribute("anotherChooseProduct", "Bạn chưa chọn sản phẩm để thanh toán");
            return "redirect:/add-to-cart/view-cart";
        }

        for (Integer selectProduct : selectProducts) {
            for (ProductInCart productInCart : listCartDetail) {
                if (selectProduct.intValue() == productInCart.getId()) {
                    productInCarts.add(productInCart);
                    break;
                }
            }
        }

        for (ProductInCart productInCart : productInCarts) {
            for (Product product : products) {
                if (productInCart.getQuantity() > product.getQuantity() && productInCart.getId() == product.getId()) {
                    redirectAttributes.addFlashAttribute("quantityGreater", "Mặt hàng " + product.getName() + " còn " + product.getQuantity() + " không đủ đáp ứng nhu cầu quý khách");
                    return "redirect:/add-to-cart/view-cart";
                }
            }
        }


        session.setAttribute("productCartPayment", productInCarts);
        model.addAttribute("productInCarts", productInCarts);
        return "sell/payment-cart";
    }

    @GetMapping("/add-to-cart/payment")
    public String paymentProductInCart() {

        List<ProductInCart> productInCarts = (List<ProductInCart>) session.getAttribute("productCartPayment");

        int userId = UserDetail.getId();
        User user = userService.findById(userId);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductInCart pic : productInCarts) {
            totalAmount = totalAmount.add(pic.getPrice().multiply(BigDecimal.valueOf(pic.getQuantity())));
        }

        Invoice invoice = new Invoice(user, totalAmount, new Date(), true);
        invoiceService.saveInvoice(invoice);

        Product product = new Product();
        for (ProductInCart pic : productInCarts) {
            product.setId(pic.getId());
            InvoiceDetail invoiceDetail = new InvoiceDetail(product, pic.getQuantity(),
                    pic.getPrice().multiply(BigDecimal.valueOf(pic.getQuantity())), true);
            invoice.addInvoiceDetail(invoiceDetail);
            invoiceDetailService.saveInvoiceDetail(invoiceDetail);

            // Update quantity product
            Product pDb = productService.findById(pic.getId());
            productService.updateQtyProduct(pDb.getId(), pDb.getQuantity() - pic.getQuantity());

            // Delete product after payment
            int cartId = cartService.findCartByUser(UserDetail.getId());
            cartDetailService.deleteByProductIdAndCartId(pic.getId(), cartId);
        }

        session.removeAttribute("productCartPayment");
        return "sell/thank-you";
    }

    @GetMapping("/add-to-cart/delete-product/{productId}")
    public String deleteProductInCart(@PathVariable("productId") Integer productId) {

        cartDetailService.deleteByProductIdAndCartId(productId, cartService.findCartByUser(UserDetail.getId()));

        return "redirect:/add-to-cart/view-cart";
    }
}
