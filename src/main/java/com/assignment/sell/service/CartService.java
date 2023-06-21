package com.assignment.sell.service;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Cart;
import com.assignment.sell.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService{

    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAllCart() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findByIdCart(Integer id) {
        return cartRepository.findById(id).get();
    }

    @Override
    public int findCartByUser(Integer id) {
        return cartRepository.findCartByUser(id);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Integer id) {
        cartRepository.deleteById(id);
    }
}
