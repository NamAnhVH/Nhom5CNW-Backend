package com.WebLearning.WebLearning.formData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String username;
    private String fullname;
    private String password;
    private String role;
}
