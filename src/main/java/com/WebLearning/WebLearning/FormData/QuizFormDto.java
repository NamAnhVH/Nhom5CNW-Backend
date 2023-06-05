package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizFormDto {

    private String question;
    private int number;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;

    private Long id;
}
