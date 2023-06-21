package com.assignment.user.service;

import com.assignment.entity.Role;
import com.assignment.entity.User;

import java.util.List;

public interface IUserService {

    List<Role> findAllRole();

    Role findByIdRole(Integer id);

    List<User> findAll();

    User findById(Integer id);

    User findByEmail(String email);

    User saveUser(User user);

    User updatePassword(User user);

    void deleteUser(Integer id);
}
