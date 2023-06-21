package com.assignment;

import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.sql.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void addUserWithRoleAdmin() {
        Role roleAdmin = entityManager.find(Role.class, 1);

        User user = new User("Do Hong Kien", new Date(2003-12-28), 1, "dohongkien2003@gmail.com");
        user.addRole(roleAdmin);

        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    public void addUserWithTwoRole() {
        Role guestRole = entityManager.find(Role.class, 2);
        Role userRole = entityManager.find(Role.class, 3);

        User user = new User("Nguyen Ngoc Linh", new Date(2003-12-28), 1, "nguyenngoclinh2003@gmail.com");
        user.addRole(guestRole);
        user.addRole(userRole);

        User saveUsers = userRepository.save(user);
        assertThat(saveUsers.getId()).isGreaterThan(0);
    }
}
