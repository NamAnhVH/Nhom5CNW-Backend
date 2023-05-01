package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelUser;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(path = "register")
//http:localhost:8080/register
public class RegisterController {

    @Autowired
    private UserService userService;

//    public boolean isRegister;

    @GetMapping("registerForm")
    //http://localhost:8080/register/registerForm
    public String registerForm(Model model) {
        //Hiện trang đăng ký
        model.addAttribute("newUser", new ModelUser());
        return "register";
    }
    @PostMapping("registerForm")
    // action http://localhost:8080/register/registerForm
    public String registerSubmit(@ModelAttribute ModelUser newUserModel , Model model) throws IOException {
        //Xử lý đăng ký tài khoản

        if(userService.isExistedAccount(newUserModel.getUsername())){
            return "redirect:/register/registerForm?existAccount=true";
        }
        //Nếu tồn tại tài khoản thì trả về trang http:localhost:8080/register/registerForm?existAccount=true

        //Lưu user vào database và trả về trang http:localhost:8080/register/registerForm?success=true
        ModelUser newUser = new ModelUser();
        newUser.setFullname(newUserModel.getFullname());
        newUser.setUsername(newUserModel.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUserModel.getPassword()));
        newUser.setRole(newUserModel.getRole());
        userService.save(newUser);
        return "redirect:/register/registerForm?success=true";
    }

}
