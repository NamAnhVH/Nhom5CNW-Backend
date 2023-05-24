package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.TeacherProfile;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.TeacherProfileRepository;
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
    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    public void addCourse(CourseFormDto newCourse) throws IOException {
        Course course = new Course();
        course.setName(newCourse.getName());
        course.setImage(newCourse.getImage().getBytes());
        course.setTime(newCourse.getTime());
        course.setIntroduction(newCourse.getIntroduction());
        course.setDescription(newCourse.getDescription());
        course.setCourseType(newCourse.getCourseType());
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        course.setTeacher(currentTeacher);
        courseRepository.save(course);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public List<Course> getCourseByOption(String option) {
        List<Course> listCourse = new ArrayList<>();
        if(option.equals("unapprovedCourse")){
            listCourse = courseRepository.findByApprovedFalse();
        } else if (option.equals("lockedCourse")) {
            listCourse = courseRepository.findByLockedTrue();
        }
        return listCourse;
    }

    public List<Course> getCourseByCourseType(String type) {
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

    public List<Course> getAllByCurrentAccount() {
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        return courseRepository.findByTeacherId(currentTeacher.getId());
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
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        Course course = courseRepository.findById(id).get();
        return course.getTeacher().getId() == currentTeacher.getId();
    }

    public List<Course> getAllUnapprovedCourseByCurrentTeacher() {
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        return courseRepository.findByApprovedFalseAndTeacherId(currentTeacher.getId());
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

    public List<CourseFormDto> getTopSixCourseApprovedAndUnlocked() {
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

    public List<CourseFormDto> getAllCourseApprovedAndUnlocked() {
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

    public List<CourseFormDto> getCourseByType(String type) {
        List<Course> listCourse = courseRepository.findByCourseType(type);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
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

    public List<CourseFormDto> getCourseBySameType(Long id) {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        Course course = courseRepository.findById(id).get();
        List<Course> listCourse = courseRepository.findByCourseType(course.getCourseType());
        for (Course relatedCourse: listCourse){
            if(relatedCourse.getId() != course.getId()){
                CourseFormDto courseDto = new CourseFormDto();
                courseDto.setName(relatedCourse.getName());
                courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(relatedCourse.getImage()));
                courseDto.setIntroduction(relatedCourse.getIntroduction());
                courseDto.setTime(relatedCourse.getTime());
                courseDto.setId(relatedCourse.getId());
                listCourseDto.add(courseDto);
            }
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByTeacher(Long id) {
        List<Course> listCourse = courseRepository.findByTeacherId(id);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
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
}
