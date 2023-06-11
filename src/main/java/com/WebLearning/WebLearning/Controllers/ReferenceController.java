package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.ReferenceFormDto;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.ReferenceService;
import com.WebLearning.WebLearning.Service.StudentProfileService;
import com.WebLearning.WebLearning.Service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ReferenceController {

    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StudentProfileService studentProfileService;
    @Autowired
    private TeacherProfileService teacherProfileService;

    @GetMapping("/referenceManage")
    public String referenceManagePage(Model model){
        model.addAttribute("listReference", referenceService.getByCurrentAccount());
        model.addAttribute("listReferencePage", true);
        return "allUser/referenceManagePage";
    }

    @GetMapping("/referenceManage/addReference")
    public String addReferencePage(Model model){
        model.addAttribute("addReferencePage", true);
        model.addAttribute("newReference", new ReferenceFormDto());
        return "allUser/referenceManagePage";
    }

    @PostMapping("/referenceManage/addReference")
    public String addReferenceAction(@ModelAttribute("newReference") ReferenceFormDto referenceFormDto){
        referenceService.addReference(referenceFormDto);
        return "redirect:/referenceManage";
    }

    @GetMapping("/referenceManage/{id}/editReference")
    public String editReferencePage(@PathVariable Long id, Model model){
        if(referenceService.isAllowed(id)){
            model.addAttribute("editReferencePage", true);
            model.addAttribute("reference", referenceService.getById(id));
            return "allUser/referenceManagePage";
        }
        return "redirect:/referenceManage";
    }

    @PostMapping("/referenceManage/{id}/editReference")
    public String editReferenceAction(@PathVariable Long id, @ModelAttribute("reference") ReferenceFormDto referenceFormDto){
        if(referenceService.isAllowed(id)){
            referenceService.updateReference(id, referenceFormDto);
        }
        return "redirect:/referenceManage";
    }

    @PostMapping("/referenceManage/{id}/deleteReference")
    public String deleteReferenceAction(@PathVariable Long id){
        if(referenceService.isAllowed(id)){
            referenceService.deleteReference(id);
        }
        return "redirect:/referenceManage";
    }

    @GetMapping("/reference")
    public String referencePage(Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("listReference", referenceService.getAllByAccountUnlocked());
        model.addAttribute("referencePage", true);
        return "allUser/listContentPage";
    }

    @GetMapping("/reference/{id}")
    public String referenceDetailPage(@PathVariable Long id, Model model){
        if(accountService.isAuthenticated()){
            if(accountService.getCurrentAccount().getRole().equals("giáo viên")){
                model.addAttribute("user", teacherProfileService.getFullnameAndAvatar());
            } else {
                model.addAttribute("user", studentProfileService.getFullnameAndAvatar());
            }
        }
        model.addAttribute("findCourse", new String());
        model.addAttribute("reference", referenceService.getReferenceById(id));
        return "allUser/referencePage";
    }
}
