package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Repository.ReferenceRepository;
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
    @Autowired
    private ReferenceService referenceService;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listNews", newsService.getTopSixNews());
        model.addAttribute("listTeacherProfile", teacherProfileService.getTopSixTeacherApprovedAndUnlocked());
        model.addAttribute("listCourse", courseService.getTopSixCourseApprovedAndUnlockedAndTeacherProfileUnlocked());
        model.addAttribute("listReference", referenceService.getTopSixReferenceAndAccountUnlocked());
        return "allUser/homepage";
    }

    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("username", accountService.getCurrentAccount().getUsername());
        model.addAttribute("email", accountService.getEmailCurrentAccount());
        model.addAttribute("accountPage", "accountPage");
        return "allUser/accountPage";
    }

    @GetMapping("/student")
    public String studentPage(Model model){
        model.addAttribute("username", accountService.getCurrentAccount().getUsername());
        model.addAttribute("email", accountService.getEmailCurrentAccount());
        model.addAttribute("accountPage", "accountPage");
        return "allUser/accountPage";
    }

    @GetMapping("/teacher")
    public String teacherPage(Model model) {
        model.addAttribute("username", accountService.getCurrentAccount().getUsername());
        model.addAttribute("email", accountService.getEmailCurrentAccount());
        model.addAttribute("accountPage", "accountPage");
        return "allUser/accountPage";
    }
}
