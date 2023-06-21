package com.assignment.user.controller;

import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewHome(Model model) {
        List<User> listUsers = userService.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users/home-user";
    }

    @GetMapping("/view-add")
    public String viewAddUser(Model model) {
        List<Role> roles = userService.findAllRole();

        model.addAttribute("roles", roles);
        model.addAttribute("user", new User());
        return "users/add-user";
    }

    @PostMapping("/add")
    public String addUser(User user, RedirectAttributes redirectAttributes) {

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Lưu thành công thông tin người dùng");
        return "redirect:/users";
    }

    @GetMapping("/detail/{id}")
    public String viewUpdateUser(@PathVariable(value = "id") Integer id, Model model) {

        List<Role> roles = userService.findAllRole();
        User user = userService.findById(id);

        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "users/update-user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "Lưu thành công thông tin người dùng");
        return "redirect:/users";
    }
}
