package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.CourseFormDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CourseService {

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
        course.setApproved(false);
        courseRepository.save(course);
    }
}
