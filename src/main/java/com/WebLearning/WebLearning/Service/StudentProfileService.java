package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentProfileService {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public String getFullname() {
        Account currentAccount = authenticationFacade.getAccount();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(currentAccount.getId());
        return studentProfile.getFullname();
    }

    public List<StudentProfile> getAllProfile() {
        return studentProfileRepository.findAll();
    }
}
