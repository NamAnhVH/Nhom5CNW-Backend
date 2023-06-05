package com.WebLearning.WebLearning.Models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column
    private String question;
    @Column
    private String answer1;
    @Column
    private String answer2;
    @Column
    private String answer3;
    @Column
    private String answer4;
    @Column
    private String correctAnswer;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lectureId", nullable = false)
    private Lecture lecture;
}
