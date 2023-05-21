package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "login")
//http://localhost:8080/login
public class LoginController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("loginForm")
    //http://localhost:8080/login/loginForm
    public String loginForm(Model model) {
        //Hiện trang Đăng nhập
        if (authenticationFacade.isAuthenticated()) {
            return "redirect:/homepage";
        }
        model.addAttribute("newUser", new Account());
        return "allUser/login";
    }

    @PostMapping("loginForm")
    // action http://localhost:8080/login/loginForm
    public String loginSubmit(@ModelAttribute("user") Account user, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
        return "allUser/login";
    }
}
