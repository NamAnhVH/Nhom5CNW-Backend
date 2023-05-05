package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Models.ModelNews;
import com.WebLearning.WebLearning.Service.NewsService;
import com.WebLearning.WebLearning.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;


@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private NewsService newsService;

    @GetMapping
    public String adminPage(){
        return "adminPage/adminPage";
    }

    @GetMapping("/addNews")
    //http://localhost:8080/admin/addNews
    public String addNewsPage(Model model){

        model.addAttribute("news", new ModelNews());
        return "adminPage/addNews";
    }

    @PostMapping("/addNews")
    public String addNewsAction(@ModelAttribute ModelNews modelNews) throws IOException {

        newsService.add(modelNews);
        return "redirect:/admin";
    }

    @GetMapping("/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.findAll());
        return "adminPage/listNewsPage";
    }

    @GetMapping("/editNews/{id}")
    public String editNewsPage(@PathVariable Long id, Model model){
        ModelNews news = newsService.findById(id);
        model.addAttribute("news", news);
        return "adminPage/editNews";
    }

    @PostMapping("/editNews/{id}")
    public String editNewsAction(@PathVariable Long id, @ModelAttribute ModelNews modelNews){
        newsService.update(id, modelNews);
        return "redirect:/admin/listNews";
    }

    @PostMapping("/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id){
        newsService.delete(id);
        return "redirect:/admin/listNews";
    }

}
