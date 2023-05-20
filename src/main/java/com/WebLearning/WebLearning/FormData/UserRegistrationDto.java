package com.WebLearning.WebLearning.FormData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String username;
    private String fullname;
    private String password;
    private String email;
    private String role;
}
