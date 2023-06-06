package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnswerFormDto {

    private List<String> answers;
    public AnswerFormDto() {
        this.answers = new ArrayList<>();
    }
}
