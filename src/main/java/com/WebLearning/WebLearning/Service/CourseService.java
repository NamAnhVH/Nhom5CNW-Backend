package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private CourseRepository courseRepository;

    public void addCourse(CourseFormDto newCourse) throws IOException {
        Course course = new Course();
        course.setName(newCourse.getName());
        course.setImage(newCourse.getImage().getBytes());
        course.setTime(newCourse.getTime());
        course.setIntroduction(newCourse.getIntroduction());
        course.setDescription(newCourse.getDescription());
        course.setCourseType(newCourse.getCourseType());
        course.setAccount(authenticationFacade.getAccount());
        courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findCourseByOption(String option) {
        List<Course> listCourse = new ArrayList<>();
        if(option.equals("unapprovedCourse")){
            listCourse = courseRepository.findByApprovedFalse();
        } else if (option.equals("lockedCourse")) {
            listCourse = courseRepository.findByLockedTrue();
        }
        return listCourse;
    }

    public List<Course> findCourseByCourseType(String type) {
        return courseRepository.findByCourseType(type);
    }

    public List<Course> findCourseByOptionAndCourseType(String option, String type) {
        List<Course> listCourse = new ArrayList<>();
        if(option.equals("unapprovedCourse")){
            listCourse = courseRepository.findByCourseTypeAndApprovedFalse(type);
        } else if (option.equals("lockedCourse")) {
            listCourse = courseRepository.findByCourseTypeAndLockedTrue(type);
        }
        return listCourse;
    }

    public void approveCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setApproved(true);
        courseRepository.save(course);
    }

    public void lockCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setLocked(true);
        courseRepository.save(course);
    }

    public void unlockCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setLocked(false);
        courseRepository.save(course);
    }

    public List<Course> findAllByCurrentAccount() {
        Account currentAccount = authenticationFacade.getAccount();
        return courseRepository.findByAccountId(currentAccount.getId());
    }

    public CourseFormDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).get();
        CourseFormDto courseDto = new CourseFormDto();
        courseDto.setName(course.getName());
        courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
        courseDto.setIntroduction(course.getIntroduction());
        courseDto.setDescription(course.getDescription());
        courseDto.setTime(course.getTime());
        courseDto.setId(course.getId());
        return courseDto;
    }

    public boolean isAccessAllowed(Long id) {
        Account currentAccount = authenticationFacade.getAccount();
        Course course = courseRepository.findById(id).get();
        return course.getAccount().getId() == currentAccount.getId();
    }

    public List<Course> findAllUnapprovedCourseByCurrentAccount() {
        Account currentAccount = authenticationFacade.getAccount();
        return courseRepository.findByApprovedFalseAndAccountId(currentAccount.getId());
    }

    public void updateCourse(Long id, CourseFormDto courseDto) throws IOException {
        Course course = courseRepository.findById(id).get();
        course.setName(courseDto.getName());
        if(!courseDto.getImage().isEmpty()){
            course.setImage(courseDto.getImage().getBytes());
        }
        course.setTime(courseDto.getTime());
        course.setIntroduction(courseDto.getIntroduction());
        course.setDescription(courseDto.getDescription());
        course.setCourseType(courseDto.getCourseType());
        course.setApproved(false);
        courseRepository.save(course);
    }

    public boolean isApprovedAndUnlocked(Long id) {
        Course course = courseRepository.findById(id).get();
        if(course.isApproved() && !course.isLocked()){
            return true;
        }
        return false;
    }

    public List<CourseFormDto> findTopSixCourseApprovedAndUnlocked() {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        List<Course> listCourse = courseRepository.findTop6ByApprovedTrueAndLockedFalseOrderByIdDesc();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(course.getTime());
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> findAllCourseApprovedAndUnlocked() {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        List<Course> listCourse = courseRepository.findByApprovedTrueAndLockedFalseOrderByIdDesc();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(course.getTime());
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
