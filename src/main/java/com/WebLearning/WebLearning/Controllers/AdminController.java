package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelNews;
import com.WebLearning.WebLearning.Service.NewsService;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public String adminPage(){
        return "adminPage";
    }

    @GetMapping("/addNews")
    //http://localhost:8080/admin/addNews
    public String addNewsPage(Model model){

        model.addAttribute("news", new ModelNews());
        return "addNews";
    }

    @PostMapping("/addNews")
    public String addNewsAction(@ModelAttribute ModelNews modelNews, Model model) throws IOException {

        ModelNews news = new ModelNews();;
        news.setTitle(modelNews.getTitle());
        news.setTime(modelNews.getTime());
        news.setImagePage(modelNews.getImagePage());
        news.setDescription(modelNews.getDescription());
        news.setDetail(modelNews.getDetail());
        newsService.save(news);
        return "redirect:/admin";
    }

    @GetMapping("/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.findAll());
        return "listNewsPage";
    }


}
