package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Email.EmailService;
import com.WebLearning.WebLearning.FormData.StudentProfileDto;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public String studentPage(){
        return "student/studentPage";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @RequestParam String edit){
        model.addAttribute("user", studentProfileService.getCurrentAccountProfile(edit));
        model.addAttribute("email", accountService.getEmailCurrentAccount());
        return "student/profileManager/profilePage";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute StudentProfileDto profileDto) throws IOException, ParseException {
        studentProfileService.updateProfile(profileDto);
        return "redirect:/student/profile?edit=false";
    }


}
