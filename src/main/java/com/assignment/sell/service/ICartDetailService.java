package com.assignment.sell.service;

import com.assignment.dto.ProductInCart;
import com.assignment.entity.CartDetail;

import java.math.BigDecimal;
import java.util.List;

public interface ICartDetailService {

    List<CartDetail> findAllCartDetail();

    List<ProductInCart> findAllCartDetailByUser(Integer userId);

    CartDetail findByIdCartDetail(Integer id);

    CartDetail saveCartDetail(CartDetail cartDetail);

    void deleteCartDetail(Integer id);

    CartDetail findByProductInCart(Integer productId, Integer cartId);

    void updateQuantityAndPriceInCart(Integer productId, Integer cartDetailId, Integer quantity, BigDecimal price);

    void deleteByProductIdAndCartId(Integer productId, Integer cartId);
}
