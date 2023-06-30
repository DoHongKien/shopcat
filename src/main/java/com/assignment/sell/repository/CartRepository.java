package com.assignment.sell.repository;

import com.assignment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c.id FROM Cart c WHERE c.user.id = :userId")
    int findCartByUser(@Param("userId") Integer userId);

    @Query("SELECT count(cd.id) FROM Cart c JOIN CartDetail cd ON c.id = cd.cart.id WHERE c.user.id = ?1")
    int countCartDetailByUser(Integer userId);
}
