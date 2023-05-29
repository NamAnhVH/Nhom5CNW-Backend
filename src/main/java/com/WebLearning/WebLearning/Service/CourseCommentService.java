package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.CourseComment;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Repository.CourseCommentRepository;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseCommentService {

    @Autowired
    private CourseCommentRepository courseCommentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    public List<CourseCommentDto> getAllCommentByCourseId(Long id) {
        List<CourseComment> listComment = courseCommentRepository.findByCourseId(id);
        List<CourseCommentDto> listCommentDto = new ArrayList<>();
        for(CourseComment comment : listComment){
            CourseCommentDto commentDto = new CourseCommentDto();
            commentDto.setFullname(comment.getStudentProfile().getFullname());
            commentDto.setTime(comment.getTime().toString());
            commentDto.setRate(String.valueOf(comment.getRate()));
            commentDto.setComment(comment.getComment());
            listCommentDto.add(commentDto);
        }
        return listCommentDto;
    }

    public void addComment(CourseCommentDto commentDto, Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        CourseComment comment = new CourseComment();
        comment.setCourse(course);
        comment.setStudentProfile(studentProfile);
        comment.setTime(LocalDateTime.now());
        comment.setComment(commentDto.getComment());
        comment.setRate(Integer.parseInt(commentDto.getRate()));
        courseCommentRepository.save(comment);
    }
}
