package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private CourseService courseService;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listNews", newsService.getTopSixNews());
        model.addAttribute("listTeacherProfile", teacherProfileService.getTopSixTeacherApprovedAndUnlocked());
        model.addAttribute("listCourse", courseService.getTopSixCourseApprovedAndUnlockedAndTeacherProfileUnlocked());
        return "allUser/homepage";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin/adminPage";
    }

    @GetMapping("/student")
    public String studentPage(){
        return "student/studentPage";
    }

    @GetMapping("/teacher")
    public String teacherPage() {
        return "teacher/teacherPage";
    }
}
