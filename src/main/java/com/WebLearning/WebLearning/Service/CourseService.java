package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
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

    public void add(CourseFormDto newCourse) throws IOException {
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
}
