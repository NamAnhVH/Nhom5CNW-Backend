package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class CourseController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private CourseCommentService courseCommentService;

    @GetMapping("/course")
    //http://localhost:8080/course
    public String coursePage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listCourse", courseService.getAllCourseApprovedAndUnlockedAndTeacherUnlocked());
        model.addAttribute("coursePage", "Danh sách khoá học");
        return "allUser/listContentPage";
    }

    @PostMapping("/course/findCourse")
    public String findCourseAction(@ModelAttribute("findCourse") String findCourse, Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listCourse", courseService.getCourseByFindCourse(findCourse));
        model.addAttribute("coursePage",true);
        return "allUser/listContentPage";
    }

    @GetMapping("/course/")
    //http://localhost:8080/course?type=
    public String courseTypePage(@RequestParam("type") String type, Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("type", type);
        model.addAttribute("listCourse", courseService.getCourseByType(type));
        model.addAttribute("coursePage", "Danh sách khoá học");
        return "allUser/listContentPage";
    }

    @GetMapping("/course/{id}")
    //http://localhost:8080/course/{id}
    public String courseDetailPage(@PathVariable Long id, Model model) {
        if(accountService.isAuthenticated()){
            if(accountService.isAuthenticated()){
                if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                    model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
                } else {
                    model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
                }
            }
            if(accountService.getCurrentAccount().getRole().equals("admin")){
                model.addAttribute("findCourse", new String());
                model.addAttribute("course", courseService.getCourseById(id));
                model.addAttribute("listLecture", lectureService.getLectureByCourseId(id));
                model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
                model.addAttribute("averageRate", courseCommentService.calAverageRate(id));
                model.addAttribute("listComment", courseCommentService.getAllCommentByCourseId(id));
                model.addAttribute("listRelatedCourse", courseService.getTop3CourseBySameType(id));
                model.addAttribute("isEnrolled", true);
                return "allUser/coursePage";
            }
        }
        if(!courseService.isApprovedAndUnlockedAndTeacherUnlocked(id)){
            return "redirect:/homepage";
        }
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                if(studentProfileService.isEnrolled(id)){
                    model.addAttribute("isEnrolled", true);
                    if(studentProfileService.isAllowed(id)){
                        model.addAttribute("isAllowed", true);
                    }
                    if(studentProfileService.isComment(id)){
                        model.addAttribute("isComment", true);
                        model.addAttribute("userComment", courseCommentService.getCommentByCurrentUser(id));
                    } else {
                        model.addAttribute("newComment", new CourseCommentDto());
                    }
                }
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("listLecture", lectureService.getLectureByCourseId(id));
        model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
        model.addAttribute("averageRate", courseCommentService.calAverageRate(id));
        model.addAttribute("listComment", courseCommentService.getAllCommentByCourseId(id));
        model.addAttribute("listRelatedCourse", courseService.getTop3CourseBySameType(id));
        return "allUser/coursePage";
    }

    @PostMapping("/course/{id}/enrollCourse")
    public String enrollCourseAction(@PathVariable Long id){
        studentProfileService.enrollCourse(id);
        return "redirect:/course/" + id;
    }

}
