package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {


    void deleteByStudentProfileId(Long id);

    List<CourseComment> findByCourseId(Long id);

    CourseComment findByStudentProfileIdAndCourseId(Long id, Long id1);

    List<CourseComment> findByCourseIdOrderByTimeDesc(Long id);

    void deleteByCourseId(Long id);
}
