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
    @Autowired
    private UserService userService;

    @GetMapping
    public String adminPage(){
        return "admin/adminPage";
    }

    @GetMapping("/addNews")
    //http://localhost:8080/admin/addNews
    public String addNewsPage(Model model){
        model.addAttribute("news", new ModelNews());
        return "admin/adminNewsManager/addNews";
    }

    @PostMapping("/addNews")
    public String addNewsAction(@ModelAttribute ModelNews modelNews) throws IOException {
        newsService.add(modelNews);
        return "redirect:/admin";
    }

    @GetMapping("/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.findAll());
        return "admin/adminNewsManager/listNewsPage";
    }

    @GetMapping("/editNews/{id}")
    public String editNewsPage(@PathVariable Long id, Model model){
        ModelNews news = newsService.findById(id);
        model.addAttribute("news", news);
        return "admin/adminNewsManager/editNews";
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

    @GetMapping("/listAccount")
    public String listAllAccountPage(Model model){
        model.addAttribute("listAccount", userService.findByRoleNot("admin"));
        return "admin/adminAccountManager/listAccountPage";
    }

    @GetMapping("/listAccount/{path}")
    public String listAccountPage(@PathVariable String path, Model model){
        model.addAttribute("listOption", path);
        model.addAttribute("listAccount", userService.findAccountByOption(path));
        return "admin/adminAccountManager/listAccountPage";
    }

    @PostMapping("/listAccount/approveAccount/{id}")
    public String approveAccount(@PathVariable Long id, @RequestParam("path") String path){
        userService.approveAccount(id);
        if("null".equals(path)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + path;
    }

    @PostMapping("/listAccount/lockAccount/{id}")
    public String lockAccount(@PathVariable Long id, @RequestParam("path") String path){
        userService.lockAccount(id);
        if("null".equals(path)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + path;
    }

    @PostMapping("/listAccount/unlockAccount/{id}")
    public String unlockAccount(@PathVariable Long id, @RequestParam("path") String path){
        userService.unlockAccount(id);
        if("null".equals(path)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + path;
    }
}
