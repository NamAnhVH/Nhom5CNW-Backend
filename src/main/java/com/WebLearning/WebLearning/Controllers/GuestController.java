package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Email.EmailService;
import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
//http://localhost:8080
public class GuestController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CourseCommentService courseCommentService;

    @GetMapping(value = {"/", "/homepage", "/homepage/null"})
    //http://localhost:8080/
    //http://localhost:8080/homepage
    //http://localhost:8080/homepage/null
    public String homepageGuest(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listNews", newsService.getTopSixNews());
        model.addAttribute("listTeacherProfile", teacherProfileService.getTopSixTeacherApprovedAndUnlocked());
        model.addAttribute("listCourse", courseService.getTopSixCourseApprovedAndUnlocked());
        return "allUser/homepage";
    }

    @GetMapping("/news")
    //http://localhost:8080/news
    public String newsPage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listNews", newsService.getAll());
        model.addAttribute("newsPage", "Tin tức sự kiện");
        return "allUser/listContentPage";
    }

    @GetMapping("/news/{id}")
    //http://localhost:8080/news/{id}
    public String newsDetailPage(@PathVariable Long id, Model model) {
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("news", newsService.getNewsById(id));
        model.addAttribute("findCourse", new String());
        return "allUser/newsPage";
    }
    @GetMapping("/teacherProfile")
    //http://localhost:8080/teacherProfile
    public String teacherProfilePage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
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
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
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

    @GetMapping("/course")
    //http://localhost:8080/course
    public String coursePage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listCourse", courseService.getAllCourseApprovedAndUnlocked());
        model.addAttribute("coursePage", "Danh sách khoá học");
        return "allUser/listContentPage";
    }

    @PostMapping("/course/findCourse")
    public String findCourseAction(@ModelAttribute("findCourse") String findCourse, Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
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
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
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
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("fullname", teacherProfileService.getFullname());
            } else {
                model.addAttribute("fullname", studentProfileService.getFullname());
            }
            if(accountService.getCurrentAccount().getRole().equals("admin")){
                model.addAttribute("findCourse", new String());
                model.addAttribute("course", courseService.getCourseById(id));
                model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
                model.addAttribute("listRelatedCourse", courseService.getTop3CourseBySameType(id));
                return "allUser/coursePage";
            }
        }
        if(!courseService.isApprovedAndUnlocked(id)){
            return "redirect:/homepage";
        }
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                if(studentProfileService.isEnrolled(id)){
                    model.addAttribute("isEnrolled", true);
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
        model.addAttribute("profile", teacherProfileService.getAllProfileTeachCourse(id));
        model.addAttribute("averageRate", courseCommentService.calAverageRate(id));
        model.addAttribute("listComment", courseCommentService.getAllCommentByCourseId(id));
        model.addAttribute("listRelatedCourse", courseService.getTop3CourseBySameType(id));
        return "allUser/coursePage";
    }

    @GetMapping("/account")
    public String accountPage(Model model){
        if(accountService.isAuthenticated()){
            model.addAttribute("username", accountService.getCurrentAccount().getUsername());
            model.addAttribute("email", accountService.getEmailCurrentAccount());
            model.addAttribute("accountPage", "accountPage");
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                return "student/accountManager/accountPage";
            } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
                return "teacher/accountManager/accountPage";
            } else {
                return "admin/accountManager/accountPage";
            }
        }
        return "redirect:/homepage";
    }

    @GetMapping("/account/changeEmail")
    public String changeEmailPage(Model model){
        if(accountService.isAuthenticated()){
            model.addAttribute("email", new String());
            model.addAttribute("changeEmailPage", true);
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                return "student/accountManager/accountPage";
            } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
                return "teacher/accountManager/accountPage";
            } else {
                return "admin/accountManager/accountPage";
            }
        }
        return "redirect:/homepage";
    }

    @PostMapping("/account/changeEmail")
    public String changeEmailAction(@ModelAttribute("email") String email, Model model){
        if(!accountService.isExistedEmail(email)){
            emailService.sendNoticeChangeEmailTo(email);
//            return "redirect:/account/changeEmail?waitEmail=true";
            model.addAttribute("changeEmailPage", true);
            model.addAttribute("waitEmail", true);
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                return "student/accountManager/accountPage";
            } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
                return "teacher/accountManager/accountPage";
            } else {
                return "admin/accountManager/accountPage";
            }
        }
        model.addAttribute("email", email);
        model.addAttribute("changeEmailPage", true);
        model.addAttribute("errorEmail", true);
        if(accountService.getCurrentAccount().getRole().equals("học sinh")){
            return "student/accountManager/accountPage";
        } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
            return "teacher/accountManager/accountPage";
        } else {
            return "admin/accountManager/accountPage";
        }
    }

    @GetMapping("/account/changeEmail/verify")
    public String changeEmailVerify(@RequestParam("code") String verificationCode){
        accountService.changeEmail(verificationCode);
        return "redirect:/account";
    }

    @GetMapping("/account/checkPassword")
    public String checkPasswordPage(Model model){
        if(accountService.isAuthenticated()){
            model.addAttribute("checkPasswordPage", true);
            model.addAttribute("checkPassword", new String());
            if(accountService.getCurrentAccount().getRole().equals("học sinh")){
                return "student/accountManager/accountPage";
            } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
                return "teacher/accountManager/accountPage";
            } else {
                return "admin/accountManager/accountPage";
            }
        }
        return "redirect:/homepage";
    }

    @PostMapping("/account/checkPassword")
    public String checkPasswordAction(@ModelAttribute("checkPassword") String checkPassword, Model model){
        if(accountService.checkPassword(checkPassword)){
            model.addAttribute("changePasswordPage", true);
            model.addAttribute("newPassword", new String());
        } else {
            model.addAttribute("checkPasswordPage", true);
            model.addAttribute("checkPassword", checkPassword);
            model.addAttribute("checkError", true);
        }
        if(accountService.getCurrentAccount().getRole().equals("học sinh")){
            return "student/accountManager/accountPage";
        } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
            return "teacher/accountManager/accountPage";
        } else {
            return "admin/accountManager/accountPage";
        }
    }

    @PostMapping("/account/changePassword")
    public String changePasswordAction(@ModelAttribute("newPassword") String newPassword, Model model) {
        accountService.setNewPassword(newPassword);
        model.addAttribute("success", true);
        if(accountService.getCurrentAccount().getRole().equals("học sinh")){
            return "student/accountManager/accountPage";
        } else if (accountService.getCurrentAccount().getRole().equals("giáo viên")) {
            return "teacher/accountManager/accountPage";
        } else {
            return "admin/accountManager/accountPage";
        }
    }



}
