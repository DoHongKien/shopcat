package com.assignment;

import com.assignment.dto.ProductInCart;
import com.assignment.entity.CartDetail;
import com.assignment.entity.User;
import com.assignment.sell.repository.CartDetailRepository;
import static org.assertj.core.api.Assertions.assertThat;

import com.assignment.sell.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartDetailRepositoryTest {

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void findAllCartDetailWithUserId() {
        List<ProductInCart> allCartDetailByUser = cartDetailRepository.findAllCartDetailByUser(9);
        for (ProductInCart a : allCartDetailByUser) {
            System.out.println(a);
        }

        assertThat(allCartDetailByUser.size()).isGreaterThan(0);
    }

    @Test
    public void findProductInCart() {
        CartDetail cartDetail = cartDetailRepository.findByProductInCart(13, 1);

        System.out.println(cartDetail.getId() + " | " + cartDetail.getPrice());

        assertThat(cartDetail).isNotNull();
    }


    @Test
    @Rollback
    public void updateQuantityInCart() {
        int index = cartDetailRepository.updateQuantityAndPriceInCart(13, 1, 2, BigDecimal.valueOf(2000));

        assertThat(index).isGreaterThan(0);
    }

    @Test
    public void findCartByUserId() {
        int cartByUser = cartRepository.findCartByUser(9);

        System.out.println("Cart id: " + cartByUser);
        assertThat(cartByUser).isGreaterThan(0);
    }
}
