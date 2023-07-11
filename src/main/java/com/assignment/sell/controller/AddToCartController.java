package com.assignment.sell.controller;

import com.assignment.dto.ProductInCart;
import com.assignment.dto.ProductInfo;
import com.assignment.entity.*;
import com.assignment.product.service.IProductService;
import com.assignment.review.dto.ReviewRequest;
import com.assignment.review.service.IReviewService;
import com.assignment.security.service.UserDetail;
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

    private IReviewService reviewService;

    @Autowired
    public AddToCartController(ICartService cartService,
                               ICartDetailService cartDetailService,
                               IProductService productService,
                               UserService userService,
                               IInvoiceService invoiceService,
                               IInvoiceDetailService invoiceDetailService,
                               IReviewService reviewService) {
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.productService = productService;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.invoiceDetailService = invoiceDetailService;
        this.reviewService = reviewService;
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

        int countBuyProduct = invoiceDetailService.countBuyProduct(UserDetail.getId(), productInfo.getId());
        int countReviewProduct = reviewService.countReviewProduct(UserDetail.getId(), productInfo.getId());

        List<Review> reviews = reviewService.findReviewByProduct(productInfo.getId());

        Product product = productService.findById(productInfo.getId());

        CartDetail productInCart = cartDetailService.findByProductInCart(productInfo.getId(), cartService.findCartByUser(UserDetail.getId()));

        if (productInCart != null) {

            int productId = productInfo.getId();
            int cartDetail = cartService.findCartByUser(UserDetail.getId());
            int quantity = productInfo.getQty() + productInCart.getQuantity();
            BigDecimal price = productInCart.getPrice();
            cartDetailService.updateQuantityAndPriceInCart(productId, cartDetail, quantity, price);
        } else {

            Cart cart = new Cart();
            cart.setId(cartService.findCartByUser(UserDetail.getId()));

            CartDetail cartDetail = new CartDetail();
            cartDetail.setProduct(product);
            cartDetail.setCart(cart);
            cartDetail.setQuantity(productInfo.getQty());
            cartDetail.setPrice(productInfo.getPrice());
            cartDetail.setStatus(true);

            cartDetailService.saveCartDetail(cartDetail);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewRequest", new ReviewRequest());
        model.addAttribute("product", product);
        model.addAttribute("countBuyProduct", countBuyProduct);
        model.addAttribute("countReviewProduct", countReviewProduct);
        model.addAttribute("message", "Sản phẩm đã được thêm vào giỏ hàng!");
        return "sell/product-detail";
    }

    @GetMapping("/add-to-cart/asc-quantity/{id}")
    public String updateQuantityAscInCart(@PathVariable(value = "id") Integer productId) {

        CartDetail productInCart = cartDetailService.findByProductInCart(productId, cartService.findCartByUser(UserDetail.getId()));
        int cartDetail = cartService.findCartByUser(UserDetail.getId());
        cartDetailService.updateQuantityAndPriceInCart(productId, cartDetail, productInCart.getQuantity() + 1, productInCart.getPrice());

        return "redirect:/add-to-cart/view-cart";
    }

    @GetMapping("/add-to-cart/desc-quantity/{id}")
    public String updateQuantityDescInCart(@PathVariable(value = "id") Integer productId) {

        CartDetail productInCart = cartDetailService.findByProductInCart(productId, cartService.findCartByUser(UserDetail.getId()));
        int cartDetail = cartService.findCartByUser(UserDetail.getId());
        cartDetailService.updateQuantityAndPriceInCart(productId, cartDetail, productInCart.getQuantity() - 1, productInCart.getPrice());

        return "redirect:/add-to-cart/view-cart";
    }

    @PostMapping("/add-to-cart/buy-now")
    public String cartPayment(@RequestParam(value = "selectProduct", required = false) Integer[] selectProducts,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        List<ProductInCart> productInCarts = new ArrayList<>();
        List<Product> products = productService.findAll();

        List<ProductInCart> listCartDetail = cartDetailService.findAllCartDetailByUser(UserDetail.getId());

        if (selectProducts == null) {
            redirectAttributes.addFlashAttribute("anotherChooseProduct", "Bạn chưa chọn sản phẩm để thanh toán");
            return "redirect:/add-to-cart/view-cart";
        }

        for (Integer selectProduct : selectProducts) {
            for (ProductInCart productInCart : listCartDetail) {
                if (selectProduct.intValue() == productInCart.getProductId()) {
                    productInCarts.add(productInCart);
                    break;
                }
            }
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductInCart productInCart : productInCarts) {
            totalAmount = totalAmount.add(productInCart.getPrice().multiply(BigDecimal.valueOf(productInCart.getQuantity())));
            for (Product product : products) {
                if (productInCart.getQuantity() > product.getQuantity() && productInCart.getProductId() == product.getId()) {
                    redirectAttributes.addFlashAttribute("quantityGreater", "Mặt hàng " + product.getName() + " còn " + product.getQuantity() + " không đủ đáp ứng nhu cầu quý khách");
                    return "redirect:/add-to-cart/view-cart";
                }
            }
        }


        session.setAttribute("productCartPayment", productInCarts);
        model.addAttribute("productInCarts", productInCarts);
        model.addAttribute("totalAmount", totalAmount);
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
            product.setId(pic.getProductId());
            InvoiceDetail invoiceDetail = new InvoiceDetail(product, pic.getQuantity(),
                    pic.getPrice().multiply(BigDecimal.valueOf(pic.getQuantity())), true);
            invoice.addInvoiceDetail(invoiceDetail);
            invoiceDetailService.saveInvoiceDetail(invoiceDetail);

            // Update quantity product
            Product pDb = productService.findById(pic.getProductId());
            productService.updateQtyProduct(pDb.getId(),
                    pDb.getQuantity() - pic.getQuantity(),
                    pDb.getSold() + pic.getQuantity());

            // Delete product after payment
            int cartId = cartService.findCartByUser(UserDetail.getId());
            cartDetailService.deleteByProductIdAndCartId(pic.getProductId(), cartId);
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
