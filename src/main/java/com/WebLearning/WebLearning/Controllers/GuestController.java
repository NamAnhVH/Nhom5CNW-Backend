package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.Service.NewsService;
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
        return "homepage";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        model.addAttribute("listNews", newsService.findAll());
        return "newsPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        if(authenticationFacade.isAuthenticated()){
            model.addAttribute("fullname", userService.getFullname());
        }
        News news = newsService.findById(id);
        model.addAttribute("news", news);
        return "newsDetailPage";
    }

//    @GetMapping("/index")
//    public String test(Model model){
//        ModelUser form = new ModelUser();
//        model.addAttribute("form", form);
//        return "index";
//    }
//
//    @PostMapping("/index")
//    public String postTest(@RequestParam("image") MultipartFile image) throws IOException {
//        userService.saveAvatar(image);
//        return "index";
//    }
}
