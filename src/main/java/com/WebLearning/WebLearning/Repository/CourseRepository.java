package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByApprovedFalse();

    List<Course> findByLockedTrue();

    List<Course> findByCourseType(String type);

    List<Course> findByCourseTypeAndApprovedFalse(String type);

    List<Course> findByCourseTypeAndLockedTrue(String type);

    List<Course> findByTeacherId(Long id);

    List<Course> findByApprovedFalseAndTeacherId(Long id);

    List<Course> findByTeacherIdAndApprovedTrueAndLockedFalse(Long id);

    List<Course> findTop6ByApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc();

    List<Course> findByApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc();

    List<Course> findByCourseTypeAndApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc(String type);

    List<Course> findTop3ByCourseTypeAndApprovedTrueAndLockedFalseAndTeacherAccountLockedFalseOrderByTimeDesc(String courseType);

    @Query(value = "SELECT c.* FROM courses c " +
            "WHERE c.approved = true AND c.locked = false AND c.teacher_profile_id IN " +
            "(SELECT t.id FROM teacher_profiles t INNER JOIN users a ON t.account_id = a.id WHERE a.locked = false) " +
            "ORDER BY (SELECT COUNT(*) FROM student_course sc WHERE sc.course_id = c.id) DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Course> findTop6CoursesWithMostStudents();

}
