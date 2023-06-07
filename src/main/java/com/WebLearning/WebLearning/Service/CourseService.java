package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseCommentDto;
import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Models.TeacherProfile;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Repository.TeacherProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public void addCourse(CourseFormDto newCourse) throws IOException {
        Course course = new Course();
        course.setName(newCourse.getName());
        course.setImage(newCourse.getImage().getBytes());
        course.setTime(LocalDateTime.now());
        course.setIntroduction(newCourse.getIntroduction());
        course.setDescription(newCourse.getDescription());
        course.setCourseType(newCourse.getCourseType());
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        course.setTeacher(currentTeacher);
        courseRepository.save(course);
    }

    public List<CourseFormDto> getAll() {
        List<Course> listCourse = courseRepository.findAll();
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setTeacher(course.getTeacher());
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByOption(String option) {
        List<Course> listCourse = new ArrayList<>();
        if(option.equals("unapprovedCourse")){
            listCourse = courseRepository.findByApprovedFalse();
        } else if (option.equals("lockedCourse")) {
            listCourse = courseRepository.findByLockedTrue();
        }
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setTeacher(course.getTeacher());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByCourseType(String type) {
        List<Course> listCourse = courseRepository.findByCourseType(type);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setTeacher(course.getTeacher());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> findCourseByOptionAndCourseType(String option, String type) {
        List<Course> listCourse = new ArrayList<>();
        if(option.equals("unapprovedCourse")){
            listCourse = courseRepository.findByCourseTypeAndApprovedFalse(type);
        } else if (option.equals("lockedCourse")) {
            listCourse = courseRepository.findByCourseTypeAndLockedTrue(type);
        }
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setTeacher(course.getTeacher());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
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

    public List<CourseFormDto> getAllByCurrentAccount() {
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        List<Course> listCourse = courseRepository.findByTeacherId(currentTeacher.getId());
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public CourseFormDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).get();
        CourseFormDto courseDto = new CourseFormDto();
        courseDto.setName(course.getName());
        courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
        courseDto.setIntroduction(course.getIntroduction());
        courseDto.setDescription(course.getDescription());
        courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
        courseDto.setId(course.getId());
        return courseDto;
    }

    public boolean isAccessAllowed(Long id) {
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        Course course = courseRepository.findById(id).get();
        return course.getTeacher().getId() == currentTeacher.getId();
    }

    public List<CourseFormDto> getAllUnapprovedCourseByCurrentTeacher() {
        TeacherProfile currentTeacher = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        List<Course> listCourse = courseRepository.findByApprovedFalseAndTeacherId(currentTeacher.getId());
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setApproved(course.isApproved());
            courseDto.setLocked(course.isLocked());
            courseDto.setCourseType(course.getCourseType());
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public void updateCourse(Long id, CourseFormDto courseDto) throws IOException {
        Course course = courseRepository.findById(id).get();
        course.setName(courseDto.getName());
        if(!courseDto.getImage().isEmpty()){
            course.setImage(courseDto.getImage().getBytes());
        }
        course.setIntroduction(courseDto.getIntroduction());
        course.setDescription(courseDto.getDescription());
        course.setCourseType(courseDto.getCourseType());
        course.setApproved(false);
        courseRepository.save(course);
    }

    public boolean isApprovedAndUnlockedAndTeacherUnlocked(Long id) {
        Course course = courseRepository.findById(id).get();
        if(course.isApproved() && !course.isLocked() && !course.getTeacher().getAccount().isLocked()){
            return true;
        }
        return false;
    }

    public List<CourseFormDto> getTopSixCourseApprovedAndUnlockedAndTeacherProfileUnlocked() {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        List<Course> listCourse = courseRepository.findTop6ByApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getAllCourseApprovedAndUnlockedAndTeacherUnlocked() {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        List<Course> listCourse = courseRepository.findByApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<CourseFormDto> getCourseByType(String type) {
        List<Course> listCourse = courseRepository.findByCourseTypeAndApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc(type);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getTop3CourseBySameType(Long id) {
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        Course course = courseRepository.findById(id).get();
        List<Course> listCourse = courseRepository.findTop3ByCourseTypeAndApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc(course.getCourseType());
        for (Course relatedCourse: listCourse){
            if(relatedCourse.getId() != course.getId()){
                CourseFormDto courseDto = new CourseFormDto();
                courseDto.setName(relatedCourse.getName());
                courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(relatedCourse.getImage()));
                courseDto.setIntroduction(relatedCourse.getIntroduction());
                courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
                courseDto.setId(relatedCourse.getId());
                listCourseDto.add(courseDto);
            }
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByTeacher(Long id) {
        List<Course> listCourse = courseRepository.findByTeacherIdAndApprovedTrueAndLockedFalse(id);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for (Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setName(course.getName());
            courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
            courseDto.setIntroduction(course.getIntroduction());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setId(course.getId());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByFindCourse(String findCourse) {
        List<Course> listCourse = courseRepository.findByApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc();
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            if(course.getName().contains(findCourse)){
                CourseFormDto courseDto = new CourseFormDto();
                courseDto.setName(course.getName());
                courseDto.setBase64Image("data:image/png;base64," + Base64.encodeBase64String(course.getImage()));
                courseDto.setIntroduction(course.getIntroduction());
                courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
                courseDto.setId(course.getId());
                listCourseDto.add(courseDto);
            }
        }
        return listCourseDto;
    }

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }

    public List<CourseFormDto> getCourseByStudent() {
        Account account = authenticationFacade.getAccount();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(account.getId());
        List<Course> listCourse = courseRepository.findByStudentsIdAndApprovedTrueAndLockedFalseAndTeacherAccountLockedFalse(studentProfile.getId());
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            CourseFormDto courseDto = new CourseFormDto();
            courseDto.setId(course.getId());
            courseDto.setName(course.getName());
            courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
            courseDto.setCourseType(course.getCourseType());
            courseDto.setTeacher(course.getTeacher());
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByFindTeacher(String teacher) {
        List<Course> listCourse = courseRepository.findAll();
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            if(course.getTeacher().getFullname().contains(teacher)){
                CourseFormDto courseDto = new CourseFormDto();
                courseDto.setName(course.getName());
                courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
                courseDto.setId(course.getId());
                courseDto.setTeacher(course.getTeacher());
                courseDto.setCourseType(course.getCourseType());
                courseDto.setApproved(course.isApproved());
                courseDto.setLocked(course.isLocked());
                listCourseDto.add(courseDto);
            }
        }
        return listCourseDto;
    }

    public List<CourseFormDto> getCourseByCourseTypeAndFindTeacher(String type, String teacher) {
        List<Course> listCourse = courseRepository.findByCourseType(type);
        List<CourseFormDto> listCourseDto = new ArrayList<>();
        for(Course course: listCourse){
            if(course.getTeacher().getFullname().contains(teacher)){
                CourseFormDto courseDto = new CourseFormDto();
                courseDto.setName(course.getName());
                courseDto.setTime(formatLocalDateTimeToString(course.getTime()));
                courseDto.setId(course.getId());
                courseDto.setTeacher(course.getTeacher());
                courseDto.setCourseType(course.getCourseType());
                courseDto.setApproved(course.isApproved());
                courseDto.setLocked(course.isLocked());
                listCourseDto.add(courseDto);
            }
        }
        return listCourseDto;
    }
}
