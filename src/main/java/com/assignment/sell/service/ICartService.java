package com.assignment.sell.service;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Cart;

import java.util.List;

public interface ICartService {

    List<Cart> findAllCart();

    Cart findByIdCart(Integer id);

    int countCartDetailByUser(Integer userId);

    int findCartByUser(Integer id);

    Cart saveCart(Cart cart);

    void deleteCart(Integer id);
}
