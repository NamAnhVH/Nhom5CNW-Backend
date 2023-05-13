package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Profile;
import com.WebLearning.WebLearning.Repository.ProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProfileService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ProfileRepository profileRepository;

    public TeacherProfileDto getTeacherProfile() {
        Account currentAccount = authenticationFacade.getAccount();
        Profile currentProfile = profileRepository.findByAccountId(currentAccount.getId());
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(currentProfile.getFullname());
        profileDto.setDescription(currentProfile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(currentProfile.getAvatar()));
        profileDto.setDetail(currentProfile.getDetail());
        return profileDto;
    }

    public void updateProfile(TeacherProfileDto profile) throws IOException {
        Account currentAccount = authenticationFacade.getAccount();
        Profile currentProfile = profileRepository.findByAccountId(currentAccount.getId());
        currentProfile.setFullname(profile.getFullname());
        if(!profile.getAvatar().isEmpty()){
            currentProfile.setAvatar(profile.getAvatar().getBytes());
        }
        currentProfile.setDescription(profile.getDescription());
        currentProfile.setDetail(profile.getDetail());
        profileRepository.save(currentProfile);
    }
}
