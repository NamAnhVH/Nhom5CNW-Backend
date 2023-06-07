package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.CourseService;
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

    @GetMapping("/listCourse")
    public String listCoursePage(Model model){
        model.addAttribute("listCourse", courseService.getCourseByStudent());
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        return "student/courseManager/listCoursePage";
    }

}
