package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.EmailService;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.FormData.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping(path = "register")
//http:localhost:8080/register
public class RegisterController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    @GetMapping("registerForm")
    //http://localhost:8080/register/registerForm
    public String registerForm(Model model) {
        //Hiện trang đăng ký
        model.addAttribute("newUser", new UserRegistrationDto());
        return "allUser/registerPage";
    }
    @PostMapping("registerForm")
    // action http://localhost:8080/register/registerForm
    public String registerSubmit(@ModelAttribute UserRegistrationDto newUser , Model model) throws IOException {
        //Xử lý đăng ký tài khoản
        if(accountService.isExistedAccount(newUser.getUsername())){
            if(!accountService.isVerifiedByUsername(newUser.getUsername())){
                if(accountService.isExistedEmail(newUser.getEmail())){
                    if(!accountService.isVerifiedByEmail(newUser.getEmail())) {
                        accountService.replaceAccountByEmail(newUser);
                        emailService.sendVerificationEmailByEmail(newUser.getEmail());
                        model.addAttribute("success", true);
                        return "allUser/registerPage";
                    } else {
                        model.addAttribute("newUser", newUser);
                        model.addAttribute("existEmail", true);
                        return "allUser/registerPage";
                    }
                } else {
                    accountService.replaceAccountByUsername(newUser);
                    emailService.sendVerificationEmailByUsername(newUser.getUsername());
                    model.addAttribute("success", true);
                    return "allUser/registerPage";
                }
            } else {
                model.addAttribute("newUser", newUser);
                model.addAttribute("existAccount", true);
                return "allUser/registerPage";
            }
        } else {
            if(accountService.isExistedEmail(newUser.getEmail())){
                if(!accountService.isVerifiedByEmail(newUser.getEmail())) {
                    accountService.replaceAccountByEmail(newUser);
                    emailService.sendVerificationEmailByEmail(newUser.getEmail());
                    model.addAttribute("success", true);
                    return "allUser/registerPage";
                }
                model.addAttribute("newUser", newUser);
                model.addAttribute("existEmail", true);
                return "allUser/registerPage";
            } else {
                accountService.addAccount(newUser);
                emailService.sendVerificationEmailByUsername(newUser.getUsername());
                model.addAttribute("success", true);
                return "allUser/registerPage";
            }
        }
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("code") String verificationCode, Model model) {
        try {
            // Xác minh email
            if(emailService.verifyEmail(verificationCode)){
                model.addAttribute("verifiedSuccess", true);
            } else {
                model.addAttribute("isVerified", true);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("verifiedSuccess", false);
        }
        return "allUser/registerPage";

    }

}
