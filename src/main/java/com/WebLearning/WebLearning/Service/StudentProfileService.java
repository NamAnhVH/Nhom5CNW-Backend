package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.StudentProfileDto;
import com.WebLearning.WebLearning.Models.Account;
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

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StudentProfileService {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCommentRepository courseCommentRepository;

    public String getFullname() {
        Account currentAccount = authenticationFacade.getAccount();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(currentAccount.getId());
        return studentProfile.getFullname();
    }

    public List<StudentProfile> getAllProfile() {
        return studentProfileRepository.findAll();
    }

    public StudentProfileDto getCurrentAccountProfile(String edit) {
        Account currentAccount = authenticationFacade.getAccount();
        StudentProfile currentProfile = studentProfileRepository.findByAccountId(currentAccount.getId());
        StudentProfileDto profileDto = new StudentProfileDto();
        profileDto.setFullname(currentProfile.getFullname());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(currentProfile.getAvatar()));
        profileDto.setGender(currentProfile.getGender());
        if(currentProfile.getBirthDate() != null){
            profileDto.setBirthDate(dateToString(currentProfile.getBirthDate(), edit));
        }
        profileDto.setLiteracy(currentProfile.getLiteracy());
        profileDto.setAddress(currentProfile.getAddress());
        profileDto.setHobby(currentProfile.getHobby());
        profileDto.setAchivement(currentProfile.getAchivement());
        return profileDto;
    }

    public void updateProfile(StudentProfileDto profileDto) throws IOException, ParseException {
        Account currentAccount = authenticationFacade.getAccount();
        StudentProfile profile = studentProfileRepository.findByAccountId(currentAccount.getId());
        profile.setFullname(profileDto.getFullname());
        if(!profileDto.getAvatar().isEmpty()){
            profile.setAvatar(profileDto.getAvatar().getBytes());
        }
        profile.setGender(profileDto.getGender());
        if(!profileDto.getBirthDate().equals("")){
            profile.setBirthDate(stringToDate(profileDto.getBirthDate()));
        }
        profile.setLiteracy(profileDto.getLiteracy());
        profile.setAddress(profileDto.getAddress());
        profile.setHobby(profileDto.getHobby());
        profile.setAchivement(profileDto.getAchivement());
        studentProfileRepository.save(profile);
    }

    public static String dateToString(Date date, String edit) {
        if(edit.equals("true")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(date);
        }
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    }

    public void enrollCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        if(!studentProfile.getCourses().contains(course)){
            studentProfile.getCourses().add(course);
            course.getStudents().add(studentProfile);
        }
        courseRepository.save(course);
        studentProfileRepository.save(studentProfile);
    }

    public boolean isEnrolled(Long id) {
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        return studentProfileRepository.isEnrolled(studentProfile.getId(), id);
    }

    public boolean isComment(Long id) {
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        CourseComment comment = courseCommentRepository.findByStudentProfileIdAndCourseId(studentProfile.getId(), id);
        if(comment != null){
            return true;
        }
        return false;
    }
}
