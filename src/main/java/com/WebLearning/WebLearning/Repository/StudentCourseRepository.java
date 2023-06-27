package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    void deleteByStudentId(Long id);

    StudentCourse findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<StudentCourse> findByCourseId(Long id);

    List<StudentCourse> findByStudentIdAndCourseApprovedTrueAndCourseLockedFalseAndCourseTeacherAccountLockedFalse(Long id);
}
