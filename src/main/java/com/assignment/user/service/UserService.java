package com.assignment.user.service;

import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.user.repository.RoleRepository;
import com.assignment.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements IUserService {

    private static final int USER_PER_PAGE = 5;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByIdRole(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAllUser(int pageNum, String keyword) {

        Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE);

        if (keyword != null) {

            return userRepository.findUserByName(keyword, pageable);
        }


        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        boolean isUpdating = (user.getId() != null);
        LocalDateTime now = LocalDateTime.now();

        if (isUpdating) {
            User u = userRepository.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(u.getPassword());
            } else {
                encodePassword(user);
            }
            user.setCreatedAt(u.getCreatedAt());
            user.setUpdatedAt(now);
        } else {
            encodePassword(user);
            user.setCreatedAt(now);
        }

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user) {

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private void encodePassword(User user) {
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
    }
}
