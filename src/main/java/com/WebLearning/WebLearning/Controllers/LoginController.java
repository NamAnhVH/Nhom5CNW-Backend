package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.EmailService;
import com.WebLearning.WebLearning.FormData.ForgetPasswordDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "login")
//http://localhost:8080/login
public class LoginController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    @GetMapping("loginForm")
    //http://localhost:8080/login/loginForm
    public String loginForm(Model model) {
        //Hiện trang Đăng nhập
        if (accountService.isAuthenticated()) {
            return "redirect:/homepage";
        }
        model.addAttribute("newUser", new Account());
        return "allUser/loginPage";
    }

    @PostMapping("loginForm")
    // action http://localhost:8080/login/loginForm
    public String loginSubmit(@ModelAttribute("user") Account user, Model model) {
        try {
            accountService.login(user);
            return "redirect:/homepage";
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Thông tin đăng nhập không chính xác");
        } catch (LockedException e) {
            model.addAttribute("error", "Tài khoản đã bị khoá");
        } catch (DisabledException e) {
            model.addAttribute("error", "Tài khoản chưa được xác thực");
        } catch (AccountExpiredException e) {
            model.addAttribute("error", "Tài khoản chưa được cấp quyền");
        }
        model.addAttribute("newUser", user);
        return "allUser/loginPage";
    }

    @GetMapping("/forgetPassword")
    public String forgetPasswordPage(Model model){
        model.addAttribute("checkAccount", new ForgetPasswordDto());
        return "allUser/forgetPasswordPage";
    }

    @PostMapping("/checkAccount")
    public String checkAccountAction(@ModelAttribute("checkAccount") ForgetPasswordDto forgetPasswordDto, Model model){
        if(accountService.checkAccount(forgetPasswordDto)){
            emailService.sendVerificationChangePasswordToEmail(forgetPasswordDto.getEmail());
            model.addAttribute("labelThongBao","success");
            return "allUser/forgetPasswordPage";
        }
        model.addAttribute("error", "Thông tin tài khoản không hợp lệ");
        model.addAttribute("checkAccount", forgetPasswordDto);
        return "allUser/forgetPasswordPage";
    }

    @GetMapping("/forgetPassword/")
    public String changePasswordPage(@RequestParam("code") String code, Model model){
        model.addAttribute("code",code);
        model.addAttribute("newPassword", new String());
        return "allUser/forgetPasswordPage";
    }

    @PostMapping("/forgetPassword/changePassword")
    public String changePasswordAction(@RequestParam("code") String code, @ModelAttribute("newPassword") String newPassword, Model model){
        accountService.setNewPassword(newPassword, code);
        model.addAttribute("success", true);
        return "allUser/forgetPasswordPage";
    }

}
