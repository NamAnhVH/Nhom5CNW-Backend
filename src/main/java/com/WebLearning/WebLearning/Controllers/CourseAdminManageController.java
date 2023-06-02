package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.EmailService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/admin")
public class CourseAdminManageController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/listCourse")
    public String listAllCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAll());
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/courseManager/listCoursePage";
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
        return "admin/courseManager/listCoursePage";
    }

    @GetMapping("/listCourse/{option}")
    public String listCoursePage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listCourse", courseService.getCourseByOption(option));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "admin/courseManager/listCoursePage";
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
        return "admin/courseManager/listCoursePage";
    }

    @PostMapping("/listCourse/approveCourse/{id}")
    public String approveCourse(@PathVariable Long id, @RequestParam("option") String option, @RequestParam("type") String type, Model model){
        courseService.approveCourse(id);
        emailService.sendNoticeAboutCourse(id,"approveCourse");
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
        emailService.sendNoticeAboutCourse(id,"unlockCourse");
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
        emailService.sendNoticeAboutCourse(id,"lockCourse");
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
