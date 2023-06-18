package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.CourseComment;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Repository.CourseCommentRepository;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        List<CourseComment> listComment = courseCommentRepository.findByCourseIdOrderByTimeDesc(id);
        List<CourseCommentDto> listCommentDto = new ArrayList<>();
        for(CourseComment comment : listComment){
            CourseCommentDto commentDto = new CourseCommentDto();
            commentDto.setFullname(comment.getStudentProfile().getFullname());
            commentDto.setTime(formatLocalDateTimeToString(comment.getTime()));
            commentDto.setRate(String.valueOf(comment.getRate()));
            commentDto.setComment(comment.getComment());
            commentDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(comment.getStudentProfile().getAvatar()));
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

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }

    public String calAverageRate(Long id){
        List<CourseComment> listComment = courseCommentRepository.findByCourseId(id);
        if(listComment.size() == 0){
            return null;
        }
        float amount = 0;
        float totalRate = 0;
        for(CourseComment comment : listComment){
            totalRate += comment.getRate();
            amount += 1;
        }
        return String.valueOf(Math.round(totalRate * 10.0/ amount) / 10.0);
    }

    public CourseCommentDto getCommentByCurrentUser(Long courseId) {
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        CourseComment comment = courseCommentRepository.findByStudentProfileIdAndCourseId(studentProfile.getId(), courseId);
        CourseCommentDto commentDto = new CourseCommentDto();
        commentDto.setRate(String.valueOf(comment.getRate()));
        commentDto.setComment(comment.getComment());
        return commentDto;
    }

    public void updateComment(CourseCommentDto commentDto, Long courseId) {
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        CourseComment comment = courseCommentRepository.findByStudentProfileIdAndCourseId(studentProfile.getId(), courseId);
        comment.setTime(LocalDateTime.now());
        comment.setComment(commentDto.getComment());
        comment.setRate(Integer.parseInt(commentDto.getRate()));
        courseCommentRepository.save(comment);
    }
}
