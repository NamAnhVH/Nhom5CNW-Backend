package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.NewsFormDto;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.NewsService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/admin/addNews")
    public String addNewsPage(Model model){
        model.addAttribute("news", new NewsFormDto());
        model.addAttribute("addNewsPage", true);
        return "admin/newsManager/newsManagePage";
    }

    @PostMapping("/admin/addNews")
    public String addNewsAction(@ModelAttribute NewsFormDto news) throws IOException {
        newsService.addNews(news);
        return "redirect:/admin/listNews";
    }

    @GetMapping("/admin/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.getAll());
        return "admin/newsManager/listNewsPage";
    }

    @GetMapping("/admin/editNews/{id}")
    public String editNewsPage(@PathVariable Long id, Model model){
        model.addAttribute("news", newsService.getNewsById(id));
        model.addAttribute("editNewsPage", true);
        return "admin/newsManager/newsManagePage";
    }

    @PostMapping("/admin/editNews/{id}")
    public String editNewsAction(@PathVariable Long id, @ModelAttribute NewsFormDto news){
        newsService.updateNews(id, news);
        return "redirect:/admin/listNews";
    }

    @PostMapping("/admin/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return "redirect:/admin/listNews";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listNews", newsService.getAll());
        model.addAttribute("newsPage", "Tin tức sự kiện");
        return "allUser/listContentPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("news", newsService.getNewsById(id));
        model.addAttribute("findCourse", new String());
        return "allUser/newsPage";
    }
}
