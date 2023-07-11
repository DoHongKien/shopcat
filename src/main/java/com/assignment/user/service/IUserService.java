package com.assignment.user.service;

import com.assignment.entity.Role;
import com.assignment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    List<Role> findAllRole();

    Role findByIdRole(Integer id);

    List<User> findAll();

    Page<User> findAllUser(int pageNum, String keyword);

    User findById(Integer id);

    User findByEmail(String email);

    User saveUser(User user);

    User updatePassword(User user);

    void deleteUser(Integer id);
}
