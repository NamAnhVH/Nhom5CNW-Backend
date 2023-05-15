package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.Service.NewsService;
import com.WebLearning.WebLearning.Service.ProfileService;
import com.WebLearning.WebLearning.Service.UserService;
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
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        model.addAttribute("listNews", newsService.findTopSixNews());
        model.addAttribute("listProfile", profileService.findTopSixTeacher());
        return "allUser/homepage";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        model.addAttribute("listNews", newsService.findAll());
        model.addAttribute("newsPage", "Tin tức sự kiện");
        return "allUser/listContentPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        News news = newsService.findById(id);
        model.addAttribute("news", news);
        return "allUser/newsPage";
    }
    @GetMapping("/teacherProfile")
    //http://localhost:8080/teacherProfile
    public String teacherProfilePage(Model model){
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        model.addAttribute("listProfile", profileService.findAllByRole("giáo viên"));
        model.addAttribute("profilePage","Danh sách giảng viên");
        return "allUser/listContentPage";
    }

    @GetMapping("/teacherProfile/{id}")
    //http://localhost:8080/teacherProfile/{id}
    public String teacherProfileDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        model.addAttribute("profile", profileService.findById(id));
        return "allUser/teacherProfilePage";
    }
}
