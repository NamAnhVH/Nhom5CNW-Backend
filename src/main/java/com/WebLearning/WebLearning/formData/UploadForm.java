package com.WebLearning.WebLearning.formData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadForm {

    private MultipartFile image;

}
