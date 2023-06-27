package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="student_course")
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private StudentProfile student;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @Column
    private LocalDateTime time;

    @Column
    private boolean allowed;
}
