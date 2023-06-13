package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByLectureId(Long lectureId);

    void deleteByLectureId(Long lectureId);
}
