package com.assignment.user.service;

import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.exception.UserNotFoundException;
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

    User findByVerficationCode(String code);

    User saveUser(User user);

    User updatePassword(User user);

    void updateVerificationCode(String code, String email) throws UserNotFoundException;

    void updateStatusAfterWhenVerification(User user);

    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;

    User findByPasswordToken(String passwordToken);

    void updatePassword(User user, String newPassword);

    void deleteUser(Integer id);
}
