package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelUser;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("homepage/{id}")
    //http://localhost:8080/homepage/{idUser}
    public String homepageUser(@PathVariable("id") Long id, Model model){
        ModelUser user = userService.findById(id);

//        model.addAttribute("user", user);
        model.addAttribute("id", user.getId());
        return "homepage";
    }
}
