package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    StudentProgress findByStudentIdAndLectureId(Long id, Long lectureId);

    List<StudentProgress> findByStudentId(Long id);

    void deleteByLectureId(Long lectureId);
}
