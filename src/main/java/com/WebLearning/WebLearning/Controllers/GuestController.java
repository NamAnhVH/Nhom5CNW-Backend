package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
//http://localhost:8080
public class GuestController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private CourseService courseService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("listNews", newsService.getTopSixNews());
        model.addAttribute("listTeacherProfile", teacherProfileService.getTopSixTeacherApprovedAndUnlocked());
        model.addAttribute("listCourse", courseService.getTopSixCourseApprovedAndUnlocked());
        return "allUser/homepage";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("listNews", newsService.getAll());
        model.addAttribute("newsPage", "Tin tức sự kiện");
        return "allUser/listContentPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "allUser/newsPage";
    }
    @GetMapping("/teacherProfile")
    //http://localhost:8080/teacherProfile
    public String teacherProfilePage(Model model){
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllByRoleAndApprovedAndUnlocked("giáo viên"));
        model.addAttribute("profilePage","Danh sách giảng viên");
        return "allUser/listContentPage";
    }

    @GetMapping("/teacherProfile/{id}")
    //http://localhost:8080/teacherProfile/{id}
    public String teacherProfileDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
            if(authenticationFacade.getAccount().getRole().equals("admin")){
                model.addAttribute("profile", teacherProfileService.getById(id));
                model.addAttribute("listCourse", courseService.getCourseByTeacher(id));
                return "allUser/teacherProfilePage";
            }
        }

        if(!teacherProfileService.isApprovedAndUnlocked(id)){
            return "redirect:/homepage";
        }
        model.addAttribute("profile", teacherProfileService.getById(id));
        model.addAttribute("listCourse", courseService.getCourseByTeacher(id));
        return "allUser/teacherProfilePage";
    }

    @GetMapping("/course")
    //http://localhost:8080/course
    public String coursePage(Model model){
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("listCourse", courseService.getAllCourseApprovedAndUnlocked());
        model.addAttribute("coursePage", "Danh sách khoá học");
        return "allUser/listContentPage";
    }

    @GetMapping("/course/")
    //http://localhost:8080/course?type=
    public String courseTypePage(@RequestParam("type") String type, Model model){
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("type", type);
        model.addAttribute("listCourse", courseService.getCourseByType(type));
        model.addAttribute("coursePage", "Danh sách khoá học");
        return "allUser/listContentPage";
    }

    @GetMapping("/course/{id}")
    //http://localhost:8080/course/{id}
    public String courseDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            if(authenticationFacade.getAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
            if(authenticationFacade.getAccount().getRole().equals("admin")){
                model.addAttribute("course", courseService.getCourseById(id));
                model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
                model.addAttribute("listRelatedCourse", courseService.getCourseBySameType(id));
                return "allUser/coursePage";
            }
        }
        if(!courseService.isApprovedAndUnlocked(id)){
            return "redirect:/homepage";
        }
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
        model.addAttribute("listRelatedCourse", courseService.getCourseBySameType(id));
        return "allUser/coursePage";
    }

}
