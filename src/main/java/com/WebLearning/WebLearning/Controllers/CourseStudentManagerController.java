package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import com.WebLearning.WebLearning.Service.StudentProgressService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class CourseStudentManagerController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProgressService studentProgressService;

    @GetMapping("/listCourse")
    public String listCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getCourseByStudent());
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        model.addAttribute("listCoursePage", true);
        return "student/courseManager/listCoursePage";
    }

    @GetMapping("/progress")
    public String progressPage(Model model){
        model.addAttribute("listProgress", studentProgressService.getByCurrentAccount());
        model.addAttribute("progressPage", true);
        return "student/courseManager/listCoursePage";
    }

}
