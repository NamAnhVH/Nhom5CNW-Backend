package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {


    StudentProfile findByAccountId(Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM StudentProfile s JOIN s.courses c WHERE s.id = :studentProfileId AND c.id = :courseId")
    boolean isEnrolled(Long studentProfileId, Long courseId);
}
