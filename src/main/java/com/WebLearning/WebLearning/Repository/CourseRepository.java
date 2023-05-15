package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {


}
