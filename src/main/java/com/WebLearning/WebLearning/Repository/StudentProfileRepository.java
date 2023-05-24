package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {


    StudentProfile findByAccountId(Long id);
}
