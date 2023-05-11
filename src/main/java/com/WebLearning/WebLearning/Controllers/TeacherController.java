package com.WebLearning.WebLearning.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/teacher")
public class TeacherController {
    @GetMapping
    public String teacherPage() {
        return "teacher/teacherPage";
    }

    @GetMapping("/information")
    public String teacherInformationPage() {
        return "teacher/teacherInfomationManager/informationPage";
    }

//    @GetMapping
}
