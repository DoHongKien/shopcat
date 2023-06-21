package com.assignment.sell.service;

import com.assignment.dto.ProductInCart;
import com.assignment.entity.CartDetail;
import com.assignment.sell.repository.CartDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartDetailService implements ICartDetailService{

    private CartDetailRepository cartDetailRepository;

    @Autowired
    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    @Override
    public List<CartDetail> findAllCartDetail() {
        return cartDetailRepository.findAll();
    }

    @Override
    public List<ProductInCart> findAllCartDetailByUser(Integer userId) {
        return cartDetailRepository.findAllCartDetailByUser(userId);
    }

    @Override
    public CartDetail findByIdCartDetail(Integer id) {
        return cartDetailRepository.findById(id).get();
    }

    @Override
    public CartDetail saveCartDetail(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @Override
    public void deleteCartDetail(Integer id) {
        cartDetailRepository.deleteById(id);
    }

    @Override
    public CartDetail findByProductInCart(Integer productId, Integer cartId) {
        return cartDetailRepository.findByProductInCart(productId, cartId);
    }

    @Override
    public void updateQuantityAndPriceInCart(Integer productId, Integer cartDetailId, Integer quantity, BigDecimal price) {
        cartDetailRepository.updateQuantityAndPriceInCart(productId, cartDetailId, quantity, price);
    }

    @Override
    public void deleteByProductIdAndCartId(Integer productId, Integer cartId) {
        cartDetailRepository.deleteByProductIdAndCartId(productId, cartId);
    }
}
