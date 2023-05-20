package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
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
        return "teacher/teacherProfileManager/profileManagerPage";
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

    @GetMapping("/course/listCourse")
    public String listCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAllByCurrentAccount());
        return "teacher/teacherCourseManager/listCoursePage";
    }

    @GetMapping("/course/listCourse/unapprovedCourse")
    public String listUnapprovedCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAllUnapprovedCourseByCurrentAccount());
        return "teacher/teacherCourseManager/listCoursePage";
    }

    @GetMapping("/course/addCourse")
    public String addCoursePage(Model model){
        model.addAttribute("newCourse", new CourseFormDto());
        return "teacher/teacherCourseManager/addCoursePage";
    }

    @PostMapping("/course/addCourse")
    public String addCourseAction(@ModelAttribute CourseFormDto newCourse) throws IOException {
        courseService.addCourse(newCourse);
        return "redirect:/teacher/course/listCourse";
    }

    @GetMapping("/course/{id}/previewCourse")
    public String previewCoursePage(@PathVariable Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("course", courseService.getCourseById(id));
        return "teacher/teacherCourseManager/previewCoursePage";
    }

    @GetMapping("/course/{id}/editCourse")
    public String editCoursePage(@PathVariable Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("course", courseService.getCourseById(id));
        return "teacher/teacherCourseManager/editCoursePage";
    }

    @PostMapping("/course/{id}/editCourse")
    public String editCourseAction(@PathVariable Long id, @ModelAttribute CourseFormDto courseDto) throws IOException {
        courseService.updateCourse(id, courseDto);
        return "redirect:/teacher/course/" + id + "/previewCourse";
    }

    @PostMapping("/course/{id}/deleteCourse")
    public String deleteCourseAction(@PathVariable Long id){
        courseService.deleteCourse(id);
        return "redirect:/teacher/course/listCourse";
    }
}
