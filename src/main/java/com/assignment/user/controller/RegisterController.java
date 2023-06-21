package com.assignment.user.controller;

import com.assignment.entity.Cart;
import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.sell.service.ICartService;
import com.assignment.user.service.EmailSenderService;
import com.assignment.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    @Autowired
    private HttpSession session;

    private UserService userService;

    private ICartService cartService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    public RegisterController(UserService userService, ICartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/register")
    public String viewRegister(Model model) {

        model.addAttribute("user", new User());
        return "account/register";
    }

    @PostMapping("/register/create")
    public String createAccount(User user, RedirectAttributes redirectAttributes) {

        Role role = userService.findByIdRole(3);
        user.setStatus(true);
        user.addRole(role);
        User saveUser = userService.saveUser(user);

        Cart cart = new Cart();
        cart.setUser(saveUser);
        cart.setStatus(true);
        cartService.saveCart(cart);

        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng ký tài khoản thành công");
        return "login";
    }

    @GetMapping("/forgot-password/view-forgot-password")
    public String forgotPassword() {
        return "account/forgot-password";
    }

    @PostMapping("/forgot-password/send-email")
    public String sendEmail(@RequestParam("email") String email, Model model) {

        User user = userService.findByEmail(email);

        if (user != null) {
            String otp = emailSenderService.generateOTP();
            emailSenderService.sendEmail(
                    email,
                    "Lấy lại mât khẩu",
                    "Mã OTP để lấy lại mật khẩu của bạn là: " + otp);
            model.addAttribute("otp", otp);
            session.setAttribute("idUser", user.getId());
            model.addAttribute("message", "Mã OTP đã được gửi vào gmail của bạn");
        } else {
            model.addAttribute("message", "Người dùng không tồn tại");
        }

        return "account/forgot-password";
    }

    @PostMapping("/forgot-password/confirmOTP")
    public String confirmOTP(@RequestParam("otp") String otp,
                             @RequestParam("otpsent") String otpsent,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        System.out.println("OTP: " + otp + " | " + "OTP sent: " + otpsent);
        System.out.println("True or false: " + otp.equals(otpsent));
        if (otp.equals(otpsent)) {
            model.addAttribute("user", new User());
            return "account/update-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "Mã OTP bạn nhập không chính xác.");
            return "account/forgot-password";
        }
    }

    @PostMapping("/forgot-password/update-password")
    public String updatePassword(User user,
                                 RedirectAttributes redirectAttributes) {

        String value = session.getAttribute("idUser").toString();
        int idInt = Integer.parseInt(value);

        User userbyId = userService.findById(idInt);

        user.setId(idInt);
        user.setName(userbyId.getName());
        user.setDob(userbyId.getDob());
        user.setSex(userbyId.getSex());
        user.setEmail(userbyId.getEmail());
        user.setStatus(userbyId.isStatus());
        user.setRoles(userbyId.getRoles());
        userService.saveUser(user);
        session.removeAttribute("idUser");
        redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi");
        return "login";
    }
}
