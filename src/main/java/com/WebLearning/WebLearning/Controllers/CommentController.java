package com.WebLearning.WebLearning.Controllers;

import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.Service.AccountService;
import com.WebLearning.WebLearning.Service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CommentController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CourseCommentService courseCommentService;

    @PostMapping("/course/{id}/comment")
    public String commentAction(@PathVariable Long id, @ModelAttribute("newComment") CourseCommentDto courseCommentDto){
        if(accountService.getCurrentAccount().getRole().equals("học sinh")){
            courseCommentService.addComment(courseCommentDto, id);
        }
        return "redirect:/course/" + id;
    }

    @PostMapping("/course/{id}/editComment")
    public String editCommentAction(@PathVariable Long id, @ModelAttribute("userComment") CourseCommentDto courseCommentDto){
        if(accountService.getCurrentAccount().getRole().equals("học sinh")) {
            courseCommentService.updateComment(courseCommentDto, id);
        }
        return "redirect:/course/" + id;
    }
}
