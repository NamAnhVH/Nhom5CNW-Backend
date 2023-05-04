package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelNews;
import com.WebLearning.WebLearning.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
//http://localhost:8080
public class GuestController {

    @Autowired
    private NewsService newsService;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){

        model.addAttribute("listNews", newsService.findTopSixNews());
        return "homepage";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){

        model.addAttribute("listNews", newsService.findAll());
        return "newsPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        ModelNews news = newsService.findById(id);
        model.addAttribute("news", news);
        return "newsDetailPage";
    }

}
