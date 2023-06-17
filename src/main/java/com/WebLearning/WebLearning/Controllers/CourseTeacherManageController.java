package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.LectureService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/teacher")
public class CourseTeacherManageController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/course/listCourse")
    public String listCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAllByCurrentAccount());
        return "teacher/courseManager/listCoursePage";
    }

    @GetMapping("/course/listCourse/unapprovedCourse")
    public String listUnapprovedCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getAllUnapprovedCourseByCurrentTeacher());
        return "teacher/courseManager/listCoursePage";
    }

    @GetMapping("/course/addCourse")
    public String addCoursePage(Model model){
        model.addAttribute("newCourse", new CourseFormDto());
        return "teacher/courseManager/addCoursePage";
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
        model.addAttribute("listLecture", lectureService.getLectureByCourseId(id));
        return "teacher/courseManager/previewCoursePage";
    }

    @GetMapping("/course/{id}/editCourse")
    public String editCoursePage(@PathVariable Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("course", courseService.getCourseById(id));
        return "teacher/courseManager/editCoursePage";
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

    @GetMapping("/course/{id}/listStudent")
    public String listStudentEnroll(@PathVariable Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("listStudent", studentProfileService.getStudentEnrollCourse(id));
        return "teacher/courseManager/listStudentEnrollPage";
    }
}
