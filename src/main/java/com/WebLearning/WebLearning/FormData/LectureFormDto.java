package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class LectureFormDto {
    private int number;
    private String title;
    private String description;
    private String urlVideo;

    private MultipartFile video;

    private Long id;


}
