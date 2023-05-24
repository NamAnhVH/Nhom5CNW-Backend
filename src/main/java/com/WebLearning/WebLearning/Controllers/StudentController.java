package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private TeacherProfileService teacherProfileService;

    @GetMapping
    public String studentPage(){
        return "student/studentPage";
    }

    @GetMapping("/profile")
    public String profilePage(Model model){
        model.addAttribute("user", teacherProfileService.getCurrentAccountProfile());
        return "student/profileManager/profilePage";
    }
}
