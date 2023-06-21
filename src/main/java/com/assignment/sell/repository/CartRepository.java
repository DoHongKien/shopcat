package com.assignment.sell.repository;

import com.assignment.dto.InvoiceDto;
import com.assignment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c.id FROM Cart c WHERE c.user.id = :userId")
    int findCartByUser(@Param("userId") Integer userId);
}
