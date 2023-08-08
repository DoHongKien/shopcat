package com.assignment.user.controller;

import com.assignment.entity.Cart;
import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.exception.UserNotFoundException;
import com.assignment.sell.service.ICartService;
import com.assignment.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private UserService userService;

    private ICartService cartService;

    @Autowired
    private JavaMailSender mailSender;

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
    public String createAccount(HttpServletRequest request, User user, Model model) {

        Role role = userService.findByIdRole(3);
        user.setStatus(false);
        user.addRole(role);
        User saveUser = userService.saveUser(user);

        Cart cart = new Cart();
        cart.setUser(saveUser);
        cart.setStatus(true);
        cartService.saveCart(cart);

        try {
            String code = RandomString.make(64);

            userService.updateVerificationCode(code, user.getEmail().trim());

            String verificationAccountLink = request.getRequestURL().toString()
                    .replace(request.getServletPath(), "")
                    + "/verification_account?code=" + code;

            String subject = "Xác thực tài khoản";
            String body = "<p>Xin chào, " + user.getName() + "</p>"
                    + "<p>Vừa có yêu cầu tạo tài khoản website ShopCat với email này</p>"
                    + "<p>Nếu là bạn hãy nhấn nút xác minh để hoàn tất thủ tục tạo tài khoản</p>"
                    + "<a href=\"" + verificationAccountLink + "\">Xác thực tài khoản</a>";

            sendEmail(user.getEmail().trim(), subject, body);

        } catch (MessagingException | UserNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("title", "Thông báo");
        model.addAttribute("message", "Link xác minh tài khoản đã được gửi vào email, " +
                "vui lòng vào email để tiến hành xác minh tài khoản");

        return "message";
    }

    @GetMapping("/verification_account")
    public String verificationAccount(@Param("code") String code, Model model) {

        User user = userService.findByVerficationCode(code);

        if (user == null) {
            model.addAttribute("title", "Xác thực tài khoản");
            model.addAttribute("message", "Mã không hợp lệ");
        } else {
            userService.updateStatusAfterWhenVerification(user);
            model.addAttribute("title", "Xác thực tài khoản");
            model.addAttribute("message", "Xác thực tài khoản thành công");
        }

        return "message";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "account/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendEmail(HttpServletRequest request, Model model) {

        String email = request.getParameter("email");
        String token = RandomString.make(50);

        try {
            userService.updateResetPasswordToken(token, email);

            String resetPasswordLink = request.getRequestURL().toString()
                    .replace(request.getServletPath(), "") +
                    "/reset_password?token=" + token;

            String subject = "Lấy lại mât khẩu";
            String body = "<p>Xin chào,</p>"
                    + "<p>Bạn có yêu cầu đặt lại mật khẩu</p>"
                    + "<p>Vui lòng nhấp vào link này để thay đổi mật khẩu:</p>"
                    + "<a href=\"" + resetPasswordLink + "\">Thay đổi mật khẩu</a>";

            sendEmail(email, subject, body);
            model.addAttribute("message", "Link thay đổi mật khẩu đã được gửi vào email của bạn");
        } catch (UserNotFoundException | MessagingException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "account/forgot-password";
    }

    private void sendEmail(String toEmail, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("dohongkien2003@gmail.com");
        helper.setTo(toEmail);

        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);

        System.out.println("Mail sent successfully...");
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param("token") String token, Model model) {

        User user = userService.findByPasswordToken(token);

        if (user == null) {
            model.addAttribute("title", "Thay đổi mật khẩu");
            model.addAttribute("message", "Mã không hợp lệ");
            return "message";
        }
        model.addAttribute("token", token);
        return "account/update-password";
    }

    @PostMapping("/reset_password")
    public String updatePassword(HttpServletRequest request, Model model) {

        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.findByPasswordToken(token);
        if (user == null) {
            model.addAttribute("title", "Thay đổi mật khẩu");
            model.addAttribute("message", "Token không hợp lệ");
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "Thay đổi mật khẩu thành công");
        }
        return "message";
    }
}
