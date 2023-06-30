package com.assignment.sell.repository;

import com.assignment.dto.ProductInCart;
import com.assignment.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {

    @Query("""
            SELECT new com.assignment.dto.ProductInCart(cd.product.id, cd.product.name, cd.quantity, cd.price, p) 
            FROM CartDetail cd JOIN Cart c ON cd.cart.id = c.id JOIN Product p ON cd.product.id = p.id WHERE c.user.id = :userId
            """)
    List<ProductInCart> findAllCartDetailByUser(@Param("userId") Integer userId);


    @Query("SELECT cd FROM CartDetail cd WHERE cd.product.id = :productId AND cd.cart.id = :cartId")
    CartDetail findByProductInCart(@Param("productId") Integer productId, @Param("cartId") Integer cartId);

    @Transactional
    @Modifying
    @Query("UPDATE CartDetail cd SET cd.quantity = :quantity, cd.price = :price WHERE cd.product.id = :productId AND cd.cart.id = :id")
    int updateQuantityAndPriceInCart(@Param("productId") Integer productId, @Param("id") Integer id, @Param("quantity") Integer quantity, @Param("price") BigDecimal price);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartDetail cd WHERE cd.product.id = :productId AND cd.cart.id = :cartId")
    void deleteByProductIdAndCartId(@Param("productId") Integer productId, @Param("cartId") Integer cartId);

}
