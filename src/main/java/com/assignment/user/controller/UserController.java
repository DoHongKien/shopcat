package com.assignment.user.controller;

import com.assignment.FileUploadUtil;
import com.assignment.entity.Role;
import com.assignment.entity.User;
import com.assignment.user.export.UserCsvExporter;
import com.assignment.user.export.UserExcelExporter;
import com.assignment.user.export.UserPdfExporter;
import com.assignment.user.service.UserService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewHome(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           Model model) {
        Page<User> users = userService.findAllUser(pageNum, keyword);
        List<User> listUsers = users.getContent();

        int totalPage = users.getTotalPages();
        int currentPage = users.getNumber() + 1;
        long totalItems = users.getTotalElements();

        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(startPage + 2, totalPage);

        if (startPage + 1 == totalPage && startPage > 1) {
            startPage -= 1;
        }

        model.addAttribute("listUsers", listUsers);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage - 1);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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
    public String addUser(User user, @RequestParam("imageUser") MultipartFile multipartFile,
                          RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setImage(fileName);
            User saveUser = userService.saveUser(user);

            String uploadDir = "user-image/" + saveUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getImage().isEmpty()) user.setImage(null);
            userService.saveUser(user);
        }

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
        String userImageDir = "user-image/" + id;
        FileUploadUtil.removeFile(userImageDir);

        redirectAttributes.addFlashAttribute("message", "Xóa thành công thông tin người dùng");
        return "redirect:/users";
    }

    @GetMapping("/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {

        List<User> users = userService.findAll();
        UserCsvExporter csvExporter = new UserCsvExporter();
        csvExporter.export(users, response);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        List<User> users = userService.findAll();
        UserExcelExporter excelExporter = new UserExcelExporter();
        excelExporter.export(users, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException, DocumentException {

        List<User> users = userService.findAll();
        UserPdfExporter pdfExporter = new UserPdfExporter();
        pdfExporter.export(users, response);
    }
}
