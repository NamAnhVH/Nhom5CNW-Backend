package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.Service.EmailService;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TeacherProfileService teacherProfileService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/admin/listAccount")
    public String listAllAccountPage(Model model){
        model.addAttribute("listAccount", accountService.getAccountByRoleNotAndVerified("admin"));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        model.addAttribute("listStudentProfile", studentProfileService.getAllProfile());
        return "admin/accountManager/listAccountPage";
    }

    @GetMapping("/admin/listAccount/{option}")
    public String listAccountPage(@PathVariable String option, Model model){
        model.addAttribute("option", option);
        model.addAttribute("listAccount", accountService.getAccountByOptionAndVerified(option));
        model.addAttribute("listTeacherProfile", teacherProfileService.getAllProfile());
        model.addAttribute("listStudentProfile", studentProfileService.getAllProfile());
        return "admin/accountManager/listAccountPage";
    }

    @PostMapping("/admin/listAccount/approveAccount/{id}")
    public String approveAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.approveAccount(id);
        emailService.sendNoticeTo(id,"approveAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/admin/listAccount/lockAccount/{id}")
    public String lockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.lockAccount(id);
        emailService.sendNoticeTo(id,"lockAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @PostMapping("/admin/listAccount/unlockAccount/{id}")
    public String unlockAccount(@PathVariable Long id, @RequestParam("option") String option){
        accountService.unlockAccount(id);
        emailService.sendNoticeTo(id,"unlockAccount");
        if("null".equals(option)){
            return "redirect:/admin/listAccount";
        }
        return "redirect:/admin/listAccount/" + option;
    }

    @GetMapping("/account")
    public String accountPage(Model model){
        if(accountService.isAuthenticated()){
            model.addAttribute("username", accountService.getCurrentAccount().getUsername());
            model.addAttribute("email", accountService.getEmailCurrentAccount());
            model.addAttribute("accountPage", "accountPage");
            return "allUser/accountPage";
        }
        return "redirect:/homepage";
    }

    @GetMapping("/account/changeEmail")
    public String changeEmailPage(Model model){
        if(accountService.isAuthenticated()){
            model.addAttribute("email", new String());
            model.addAttribute("changeEmailPage", true);
            return "allUser/accountPage";
        }
        return "redirect:/homepage";
    }

    @PostMapping("/account/changeEmail")
    public String changeEmailAction(@ModelAttribute("email") String email, Model model){
        if(!accountService.isExistedEmail(email)){
            emailService.sendNoticeChangeEmailTo(email);
            model.addAttribute("changeEmailPage", true);
            model.addAttribute("waitEmail", true);
            return "allUser/accountPage";
        }
        model.addAttribute("email", email);
        model.addAttribute("changeEmailPage", true);
        model.addAttribute("errorEmail", true);
        return "allUser/accountPage";
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
            return "allUser/accountPage";
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
        return "allUser/accountPage";
    }

    @PostMapping("/account/changePassword")
    public String changePasswordAction(@ModelAttribute("newPassword") String newPassword, Model model) {
        accountService.setNewPassword(newPassword);
        model.addAttribute("success", true);
        return "allUser/accountPage";
    }

    @PostMapping("/account/deleteAccount")
    public String deleteAccountAction(){
        if(accountService.getCurrentAccount().getRole().equals("học sinh")){
            accountService.deleteStudentAccount();
        } else if(accountService.getCurrentAccount().getRole().equals("giáo viên")) {
            accountService.deleteTeacherAccount();
        }
        return "redirect:/logout";
    }
}
