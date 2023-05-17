package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class CourseFormDto {
    private String name;
    private MultipartFile image;
    private String base64Image;
    private String time;
    private String introduction;
    private String description;
    private String courseType;

    private Long id;
}
