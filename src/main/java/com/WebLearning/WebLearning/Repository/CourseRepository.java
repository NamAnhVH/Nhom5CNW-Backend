package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


    List<Course> findByApprovedFalse();

    List<Course> findByLockedTrue();

    List<Course> findByCourseType(String type);

    List<Course> findByCourseTypeAndApprovedFalse(String type);

    List<Course> findByCourseTypeAndLockedTrue(String type);

    List<Course> findTop6ByApprovedTrueAndLockedFalseOrderByIdDesc();

    List<Course> findByApprovedTrueAndLockedFalseOrderByIdDesc();

    List<Course> findByTeacherId(Long id);

    List<Course> findByApprovedFalseAndTeacherId(Long id);
}
