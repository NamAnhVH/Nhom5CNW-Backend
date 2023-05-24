package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Email.EmailVerificationService;
import com.WebLearning.WebLearning.FormData.NewsFormDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
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
    private EmailVerificationService emailVerificationService;

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
        newsService.addNews(news);
        return "redirect:/admin";
    }

    @GetMapping("/listNews")
    public String listNewsPage(Model model){
        model.addAttribute("listNews", newsService.getAll());
        return "admin/adminNewsManager/listNewsPage";
    }

    @GetMapping("/editNews/{id}")
    public String editNewsPage(@PathVariable Long id, Model model){
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "admin/adminNewsManager/editNews";
    }

    @PostMapping("/editNews/{id}")
    public String editNewsAction(@PathVariable Long id, @ModelAttribute NewsFormDto news){
        newsService.updateNews(id, news);
        return "redirect:/admin/listNews";
    }

    @PostMapping("/deleteNews/{id}")
    public String deleteNews(@PathVariable Long id){
        newsService.deleteNews(id);
        return "redirect:/admin/listNews";
    }

    @GetMapping("/listAccount")
    public String listAllAccountPage(Model model){
        model.addAttribute("listAccount", accountService.getAccountByRoleNotAndVerified("admin"));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        model.addAttribute("listStudentProfile", studentProfileService.getAllProfile());
        return "admin/adminAccountManager/listAccountPage";
    }

    @GetMapping("/listAccount/{option}")
    public String listAccountPage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listAccount", accountService.getAccountByOptionAndVerified(option));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        model.addAttribute("listStudentProfile", studentProfileService.getAllProfile());
        return "admin/adminAccountManager/listAccountPage";
    }

    @PostMapping("/listAccount/approveAccount/{id}")
    public String approveAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.approveAccount(id);
        emailVerificationService.sendNoticeTo(id,"approveAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/listAccount/lockAccount/{id}")
    public String lockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.lockAccount(id);
        emailVerificationService.sendNoticeTo(id,"lockAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/listAccount/unlockAccount/{id}")
    public String unlockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.unlockAccount(id);
        emailVerificationService.sendNoticeTo(id,"unlockAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @GetMapping("/listCourse")
    public String listAllCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAll());
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/")
    public String listAllCourseByTypePage(@RequestParam("type") String type, Model model){
        if ("".equals(type)){
            model.addAttribute("listCourse", courseService.getAll());
        } else {
            model.addAttribute("listCourse", courseService.getCourseByCourseType(type));
        }
        model.addAttribute("type", type);
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/{option}")
    public String listCoursePage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listCourse", courseService.getCourseByOption(option));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @GetMapping("/listCourse/{option}/")
    public String listCourseByTypePage(@PathVariable String option, @RequestParam String type, Model model){
        if ("".equals(type)){
            model.addAttribute("listCourse", courseService.getCourseByOption(option));
        } else {
            model.addAttribute("listCourse", courseService.findCourseByOptionAndCourseType(option, type));
        }
        model.addAttribute("type", type);
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/adminCourseManager/listCoursePage";
    }

    @PostMapping("/listCourse/approveCourse/{id}")
    public String approveCourse(@PathVariable Long id, @RequestParam("option") String option, @RequestParam("type") String type, Model model){
        courseService.approveCourse(id);
        emailVerificationService.sendNoticeAboutCourse(id,"approveCourse");
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
        emailVerificationService.sendNoticeAboutCourse(id,"unlockCourse");
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
        emailVerificationService.sendNoticeAboutCourse(id,"lockCourse");
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
