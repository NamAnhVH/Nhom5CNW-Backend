package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.LectureFormDto;
import com.WebLearning.WebLearning.Models.Lecture;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.LectureService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

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
                    model.addAttribute("listLecture", lectureService.getLectureByCourseId(courseId));
                    return "student/lectureStudy/lecturePage";
                }
            }
        }
        return "redirect:/course/" + courseId;
    }

    @GetMapping(value = "/lecture/{lectureId}/videosrc", produces = "video/mp4")
    @ResponseBody
    public FileSystemResource videoSource(@PathVariable("courseId") Long courseId , @PathVariable(value="lectureId") Long lectureId,
                                          HttpServletResponse response) throws IOException {
        if(accountService.isAuthenticated()) {
            if (accountService.getCurrentAccount().getRole().equals("học sinh")) {
                if (studentProfileService.isEnrolled(courseId)) {
                    LectureFormDto lecture = lectureService.getLectureById(lectureId);
                    if(!lecture.getUrlVideo().equals("")){
                        return new FileSystemResource(new File(lecture.getUrlVideo()));
                    }
                    return null;
                }
            }
        }
        response.sendRedirect("/homepage");
        return null;
    }
}
