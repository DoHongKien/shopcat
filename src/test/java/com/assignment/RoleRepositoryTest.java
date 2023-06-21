package com.assignment;

import com.assignment.entity.Role;
import com.assignment.user.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testAddRole() {
        Role role = new Role("ADMIN", "Role admin có toàn quyền truy cập trong hệ thống", true);
        Role saveRole = roleRepository.save(role);
        assertThat(saveRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testAddAllRole() {
        Role guest = new Role(
                "GUEST",
                "Role khách có quyền tìm, xem chi tiết, thêm, bớt hàng trong giỏ, đăng ký, đăng nhập, quên mật khẩu",
                true);

        Role user = new Role(
                "USER",
                "Role người dùng có quyền tìm, xem chi tiết, thêm, bớt hàng trong giỏ, đăng ký, đăng nhập, quên mật khẩu, đăng xuất, thanh toán/mua hàng",
                true);

        roleRepository.saveAll(List.of(guest, user));
    }
}
