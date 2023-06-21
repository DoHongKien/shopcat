package com.assignment.user.controller;

import com.assignment.entity.User;
import com.assignment.security.UserDetail;
import com.assignment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetail userDetail, Model model) {

        User user = userService.findByEmail(userDetail.getUsername());

        model.addAttribute("user", user);
        return "users/profile-user";
    }

    @PostMapping("/update-profile")
    public String updateProfile(User user, RedirectAttributes redirectAttributes) {

        User u = userService.findById(user.getId());

        user.setRoles(u.getRoles());
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Đã sửa thành công thông tin người dùng");
        return "redirect:/profile";
    }
}
