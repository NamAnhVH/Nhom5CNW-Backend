package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class StudentProfileDto {

    private String fullname;
    private MultipartFile avatar;
    private String base64Avatar;
    private String gender;
    private String birthDate;
    private String literacy;
    private String address;
    private String hobby;
    private String achivement;

    private String enrollDate;
    private int number;

    private String email;

    private Long id;
}
