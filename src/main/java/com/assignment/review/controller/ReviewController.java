package com.assignment.review.controller;

import com.assignment.entity.Product;
import com.assignment.entity.Review;
import com.assignment.entity.User;
import com.assignment.review.dto.ReviewRequest;
import com.assignment.review.service.IReviewService;
import com.assignment.security.service.UserDetail;
import com.assignment.sell.service.IInvoiceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private IInvoiceDetailService invoiceDetailService;

    @GetMapping("/show-review/{productId}")
    public ResponseEntity<List<Review>> showReview(@PathVariable("productId") Integer productId, Model model) {
        List<Review> reviews = reviewService.findReviewByProduct(productId);
        model.addAttribute("reviews", reviews);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/add-review")
    public String saveReview(@ModelAttribute("reviewRequest") ReviewRequest review,
                             RedirectAttributes redirectAttributes) {

        int countBuyProduct = invoiceDetailService.countBuyProduct(UserDetail.getId(), review.getProductId());
        int countReviewProduct = reviewService.countReviewProduct(UserDetail.getId(), review.getProductId());

        Review reviewForm = new Review();

        if (review.getReviewId() != null) {
            reviewForm.setId(review.getReviewId());
        }

        User user = new User();
        user.setId(UserDetail.getId());

        Product product = new Product();
        product.setId(review.getProductId());

        reviewForm.setUser(user);
        reviewForm.setProduct(product);
        reviewForm.setRating(review.getRating());
        reviewForm.setTitle(review.getTitle());
        reviewForm.setContent(review.getContent());
        reviewForm.setCreatedAt(new Date());

        reviewService.saveReview(reviewForm);
        redirectAttributes.addFlashAttribute("countBuyProduct", countBuyProduct);
        redirectAttributes.addFlashAttribute("countReviewProduct", countReviewProduct);
        redirectAttributes.addFlashAttribute("message", "Bạn đã đánh giá thành công cho sản phẩm");
        return "redirect:/sell/detail-product/" + review.getProductId();
    }

    @GetMapping("/detail/{id}")
    public String detailReview(@PathVariable("id") Integer id, Model model) {

        Review review = reviewService.findById(id);
        model.addAttribute("reviewDetail", review);
        return "/sell/product-detail";
    }

    @GetMapping("/delete/{id}/{product}")
    public String deleteReview(@PathVariable("product") Integer productId,
                               @PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        reviewService.deleteReview(id);
        redirectAttributes.addFlashAttribute("message", "Bạn đã xóa đánh giá sản phẩm thành công");
        return "redirect:/sell/detail-product/" + productId;
    }
}
