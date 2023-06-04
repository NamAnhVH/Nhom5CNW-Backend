package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.LectureFormDto;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/teacher/course/{courseId}")
public class LectureManageController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;

    @GetMapping("/addLecture")
    public String addLecturePage(@PathVariable("courseId") Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("newLecture", new LectureFormDto());
        model.addAttribute("id", id);
        return "teacher/lectureManager/addLecturePage";
    }

    @PostMapping("/addLecture")
    public String addLectureAction(@PathVariable("courseId") Long id, @ModelAttribute LectureFormDto lectureFormDto) throws IOException {
        lectureService.addLecture(id, lectureFormDto);
        return "redirect:/teacher/course/" + id + "/previewCourse";
    }

    @GetMapping("manageLecture")
    public String manageLecturePage(@PathVariable("courseId") Long id, Model model){
        if(!courseService.isAccessAllowed(id)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("id", id);
        model.addAttribute("listLecture", lectureService.getLectureByCourseId(id));
        return "teacher/lectureManager/listLecturePage";
    }

    @GetMapping("/manageLecture/{lectureId}")
    public String editLecturePage(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, Model model){
        if(!courseService.isAccessAllowed(courseId)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("id", courseId);
        model.addAttribute("lecture", lectureService.getLectureById(lectureId));
        return "teacher/lectureManager/editLecturePage";
    }

    @PostMapping("/manageLecture/{lectureId}/editLecture")
    public String editLectureAction(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, @ModelAttribute("lecture") LectureFormDto lectureFormDto) throws IOException {
        lectureService.updateLecture(lectureId, lectureFormDto);
        return "redirect:/teacher/course/" + courseId + "/manageLecture";
    }

    @PostMapping("/manageLecture/{lectureId}/deleteLecture")
    public String deleteLectureAction(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId){
        lectureService.deleteLecture(lectureId);
        return "redirect:/teacher/course/" + courseId + "/manageLecture";
    }
}
