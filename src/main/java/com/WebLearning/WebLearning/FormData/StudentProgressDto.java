package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProgressDto {

    private String course;
    private String lecture;
    private int number;
    private float grade;
    private String time;

}
