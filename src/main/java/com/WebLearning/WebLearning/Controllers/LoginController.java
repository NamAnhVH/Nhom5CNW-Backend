package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelUser;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private UserService userService;
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
        model.addAttribute("newUser", new ModelUser());
        return "login";
    }

    @PostMapping("loginForm")
    // action http://localhost:8080/login/loginForm
    public String loginSubmit(@ModelAttribute("user") ModelUser user, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            model.addAttribute("error", "Sai tài khoản");
            model.addAttribute("newUser", user);
            return "login";
        }
        return "redirect:/homepage";
    }
}
