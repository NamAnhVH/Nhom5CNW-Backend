package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TeacherProfileDto {

    private String fullname;
    private MultipartFile avatar;
    private String base64Avatar;
    private String description;
    private String detail;


    private Long id;
//    private byte[] test;

//    public void setAvatar(byte[] avatar) {
//        ByteArrayResource resource = new ByteArrayResource(avatar);
//    }
}
