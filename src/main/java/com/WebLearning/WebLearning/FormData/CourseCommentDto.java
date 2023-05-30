package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCommentDto {

    private String fullname;
    private String time;
    private String rate;
    private String comment;
    private String base64Avatar;
}
