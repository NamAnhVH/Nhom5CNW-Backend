package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.User;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(path = "login")
//http:localhost:8080/login
public class LoginController {

    @Autowired
    private UserService userService;

//    public boolean isRegister;
    @GetMapping("loginForm")
    //http:localhost:8080/login/loginForm
    public String loginForm(Model model) {
        //Hiện trang Đăng nhập
        return "login";
    }

    @PostMapping("loginForm")
    // action http:localhost:8080/login/loginForm
    public String loginSubmit(){
        //Xử lý đăng nhập
        return "redirect:/homepage";
    }

    @GetMapping("registerForm")
    //http:localhost:8080/login/registerForm
    public String registerForm(Model model) {
        model.addAttribute("newUser", new User());
        return "register";
    }
    @PostMapping("registerForm")
    // action http:localhost:8080/login/registerForm
    public String registerSubmit(@ModelAttribute User newUserModel , Model model) throws IOException {
        //Xử lý đăng ký tài khoản

        if(userService.isExistedAccount(newUserModel.getUsername())){
            return "redirect:/login/registerForm?existAccount=true";
        }
        //Nếu tồn tại tài khoản thì trả về trang http:localhost:8080/login/registerForm?existAccount=true

        //Lưu user vào database và trả về trang http:localhost:8080/login/registerForm?success=true
        User newUser = new User();
        newUser.setHoTen(newUserModel.getHoTen());
        newUser.setUsername(newUserModel.getUsername());
        newUser.setPassword(newUserModel.getPassword());
        newUser.setRole(newUserModel.getRole());
        userService.save(newUser);
        return "redirect:/login/registerForm?success=true";
    }

}
