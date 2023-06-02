package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.LectureService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/course/{courseId}")
public class LectureStudyController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;

    @GetMapping("/lecture/{lectureId}")
    public String lecturePage(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                if(studentProfileService.isEnrolled(courseId)){
                    model.addAttribute("fullname", studentProfileService.getFullname());
                    model.addAttribute("course", courseService.getCourseById(courseId));
                    model.addAttribute("lecture", lectureService.getLectureById(lectureId));
                    return "student/lectureStudy/lecturePage";
                }
            }
        }
        return "redirect:/course/" + courseId;
    }
}
