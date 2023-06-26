package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.StudentProfileDto;
import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.CourseService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping("/")
public class ProfileController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/teacherProfile")
    public String listTeacherPage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllByRoleAndApprovedAndUnlocked("giáo viên"));
        model.addAttribute("profilePage","Danh sách giảng viên");
        return "allUser/listContentPage";
    }

    @GetMapping("/teacherProfile/{id}")
    //http://localhost:8080/teacherProfile/{id}
    public String teacherProfileDetailPage(@PathVariable Long id, Model model) {
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
                model.addAttribute("profile", teacherProfileService.getById(id));
                model.addAttribute("listCourse", courseService.getCourseByTeacher(id));
                return "allUser/teacherProfilePage";
            }
        }

        if(!teacherProfileService.isApprovedAndUnlocked(id)){
            return "redirect:/homepage";
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("profile", teacherProfileService.getById(id));
        model.addAttribute("listCourse", courseService.getCourseByTeacher(id));
        return "allUser/teacherProfilePage";
    }

    @GetMapping("/student/profile")
    public String studentProfilePage(Model model, @RequestParam String edit){
        model.addAttribute("user", studentProfileService.getCurrentAccountProfile(edit));
        model.addAttribute("email", accountService.getEmailCurrentAccount());
        return "student/profileManager/profilePage";
    }

    @PostMapping("/student/profile/edit")
    public String editStudentProfile(@ModelAttribute StudentProfileDto profileDto) throws IOException, ParseException {
        studentProfileService.updateProfile(profileDto);
        return "redirect:/student/profile?edit=false";
    }

    @GetMapping("/teacher/profile")
    public String teacherProfilePage(Model model) {
        model.addAttribute("user", teacherProfileService.getCurrentAccountProfile());
        return "teacher/profileManager/profilePage";
    }

    @PostMapping("/teacher/profile/edit")
    public String editTeacherProfile(@ModelAttribute TeacherProfileDto profile) throws IOException {
        teacherProfileService.updateProfile(profile);
        return "redirect:/teacher/profile";
    }
}
