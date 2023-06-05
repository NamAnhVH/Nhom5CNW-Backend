package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.QuizFormDto;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.QuizService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher/course/{courseId}/manageLecture/{lectureId}")
public class QuizManageController {

    @Autowired
    private QuizService quizService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/addQuiz")
    public String addQuizPage(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, Model model){
        if(!courseService.isAccessAllowed(courseId)){
            return "redirect:/teacher/course/listCourse";
        }
        model.addAttribute("listQuiz", quizService.getQuizByLectureId(lectureId));
        model.addAttribute("newQuiz", new QuizFormDto());
        model.addAttribute("courseId", courseId);
        model.addAttribute("lectureId", lectureId);
        model.addAttribute("editQuiz", new QuizFormDto());
        return "teacher/lectureManager/quizPage";
    }

    @PostMapping("/addQuiz")
    public String addQuizAction(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, @ModelAttribute("newQuiz") QuizFormDto quizFormDto){
        if(!courseService.isAccessAllowed(courseId)){
            return "redirect:/teacher/course/listCourse";
        }
        quizService.addQuiz(lectureId, quizFormDto);
        return "redirect:/teacher/course/" + courseId + "/manageLecture/" + lectureId + "/addQuiz";
    }

    @PostMapping("/editQuiz/{quizId}")
    public String editQuizAction(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, @PathVariable("quizId") Long quizId, @ModelAttribute("editQuiz") QuizFormDto quizFormDto){
        if(!courseService.isAccessAllowed(courseId)){
            return "redirect:/teacher/course/listCourse";
        }
        quizService.updateQuiz(quizId, quizFormDto);
        return "redirect:/teacher/course/" + courseId + "/manageLecture/" + lectureId + "/addQuiz";
    }

    @PostMapping("/deleteQuiz/{quizId}")
    public String deleteQuizAction(@PathVariable("courseId") Long courseId, @PathVariable("lectureId") Long lectureId, @PathVariable("quizId") Long quizId){
        if(!courseService.isAccessAllowed(courseId)){
            return "redirect:/teacher/course/listCourse";
        }
        quizService.deleteQuiz(quizId);
        return "redirect:/teacher/course/" + courseId + "/manageLecture/" + lectureId + "/addQuiz";
    }

}
