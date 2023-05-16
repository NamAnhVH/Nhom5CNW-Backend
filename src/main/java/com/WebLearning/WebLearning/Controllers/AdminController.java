package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.NewsFormDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.NewsService;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String adminPage(){
        return "admin/adminPage";
    }

    @GetMapping("/addNews")
    //http://localhost:8080/admin/addNews
    public String addNewsPage(Model model){
        model.addAttribute("news", new NewsFormDto());
        return "admin/adminNewsManager/addNews";
    }

    @PostMapping("/addNews")
    public String addNewsAction(@ModelAttribute NewsFormDto news) throws IOException {
        newsService.add(news);
        return "redirect:/admin";
    }

    @GetMapping("/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.findAll());
        return "admin/adminNewsManager/listNewsPage";
    }

    @GetMapping("/editNews/{id}")
    public String editNewsPage(@PathVariable Long id, Model model){
        News news = newsService.findById(id);
        model.addAttribute("news", news);
        return "admin/adminNewsManager/editNews";
    }

    @PostMapping("/editNews/{id}")
    public String editNewsAction(@PathVariable Long id, @ModelAttribute NewsFormDto news){
        newsService.update(id, news);
        return "redirect:/admin/listNews";
    }

    @PostMapping("/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id){
        newsService.delete(id);
        return "redirect:/admin/listNews";
    }

    @GetMapping("/listAccount")
    public String listAllAccountPage(Model model){
        model.addAttribute("listAccount", accountService.findAccountByRoleNot("admin"));
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminAccountManager/listAccountPage";
    }

    @GetMapping("/listAccount/{option}")
    public String listAccountPage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listAccount", accountService.findAccountByOption(option));
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminAccountManager/listAccountPage";
    }

    @PostMapping("/listAccount/approveAccount/{id}")
    public String approveAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.approveAccount(id);
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/listAccount/lockAccount/{id}")
    public String lockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.lockAccount(id);
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/listAccount/unlockAccount/{id}")
    public String unlockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.unlockAccount(id);
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @GetMapping("/listCourse")
    public String listAllCoursePage(Model model){
        model.addAttribute("listCourse", courseService.findAll());
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/")
    public String listAllCourseByTypePage(@RequestParam("type") String type, Model model){
        if ("".equals(type)){
            model.addAttribute("listCourse", courseService.findAll());
        } else {
            model.addAttribute("listCourse", courseService.findCourseByCourseType(type));
        }
        model.addAttribute("type", type);
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/{option}")
    public String listCoursePage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listCourse", courseService.findCourseByOption(option));
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/{option}/")
    public String listCourseByTypePage(@PathVariable String option, @RequestParam String type, Model model){
        if ("".equals(type)){
            model.addAttribute("listCourse", courseService.findCourseByOption(option));
        } else {
            model.addAttribute("listCourse", courseService.findCourseByOptionAndCourseType(option, type));
        }
        model.addAttribute("type", type);
        model.addAttribute("listProfile", profileService.findAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @PostMapping("/listCourse/approveCourse/{id}")
    public String approveCourse(@PathVariable Long id, @RequestParam("option") String option, @RequestParam("type") String type, Model model){
        courseService.approveCourse(id);
        if("null".equals(type)){
            if("null".equals(option)){
                return "redirect:/admin/listCourse";
            } else {
                return "redirect:/admin/listCourse/" + option;
            }
        } else {
            try {
                type = URLEncoder.encode(type, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if("null".equals(option)){
                return "redirect:/admin/listCourse/?type=" + type;
            } else {
                return "redirect:/admin/listCourse/" + option + "/?type=" + type;
            }
        }
    }

    @PostMapping("/listCourse/unlockCourse/{id}")
    public String unlockCourse(@PathVariable Long id, @RequestParam("option") String option, @RequestParam("type") String type, Model model){
        courseService.unlockCourse(id);
        if("null".equals(type)){
            if("null".equals(option)){
                return "redirect:/admin/listCourse";
            } else {
                return "redirect:/admin/listCourse/" + option;
            }
        } else {
            try {
                type = URLEncoder.encode(type, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if("null".equals(option)){
                return "redirect:/admin/listCourse/?type=" + type;
            } else {
                return "redirect:/admin/listCourse/" + option + "/?type=" + type;
            }
        }
    }

    @PostMapping("/listCourse/lockCourse/{id}")
    public String lockCourse(@PathVariable Long id, @RequestParam("option") String option, @RequestParam("type") String type, Model model){
        courseService.lockCourse(id);
        if("null".equals(type)){
            if("null".equals(option)){
                return "redirect:/admin/listCourse";
            } else {
                return "redirect:/admin/listCourse/" + option;
            }
        } else {
            try {
                type = URLEncoder.encode(type, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if("null".equals(option)){
                return "redirect:/admin/listCourse/?type=" + type;
            } else {
                return "redirect:/admin/listCourse/" + option + "/?type=" + type;
            }
        }
    }


}
