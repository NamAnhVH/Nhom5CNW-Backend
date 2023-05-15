package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Models.Profile;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping(path = "/teacher")
public class TeacherController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String teacherPage() {
        return "teacher/teacherPage";
    }

    @GetMapping("/profile")
    public String teacherProfilePage(Model model) {
        model.addAttribute("user", profileService.getTeacherProfile());
        return "teacher/teacherProfileManager/profilePage";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute TeacherProfileDto profile){
        try {
            profileService.updateProfile(profile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/teacher/profile";
    }

    @GetMapping("/course/addCourse")
    public String addCoursePage(Model model){
        model.addAttribute("newCourse", new CourseFormDto());
        return "teacher/teacherCourseManager/addCoursePage";
    }

    @PostMapping("/course/addCourse")
    public String addCourseAction(@ModelAttribute CourseFormDto newCourse) throws IOException {
        courseService.add(newCourse);
        return "redirect:/teacher";
    }
}
