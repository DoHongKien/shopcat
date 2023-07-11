package com.assignment.user.repository;

import com.assignment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%")
    Page<User> findUserByName(String name, Pageable pageable);

    User findUserByEmail(String email);
}
