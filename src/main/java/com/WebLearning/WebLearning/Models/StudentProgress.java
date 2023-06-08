package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student_progress")
public class StudentProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "student_profile_id")
    private StudentProfile student;

    @Column
    private int number;

    @Column
    private float grade;

    @Column
    private LocalDateTime time;
}
