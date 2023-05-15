package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Profile;
import com.WebLearning.WebLearning.Repository.ProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public TeacherProfileDto findById(Long id) {
        Profile profile = profileRepository.findById(id).get();
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(profile.getFullname());
        profileDto.setDescription(profile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
        profileDto.setDetail(profile.getDetail());
        return profileDto;
    }

    public List<TeacherProfileDto> findTopSixTeacher() {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<Profile> listProfile = profileRepository.findTop6ByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc("giao-vien");
        for (Profile profile: listProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(profile.getFullname());
            profileDto.setDescription(profile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
            profileDto.setDetail(profile.getDetail());
            profileDto.setId(profile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }

    public List<TeacherProfileDto> findAllByRole(String role) {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<Profile> listProfile = profileRepository.findByAccountRoleOrderByIdDesc(role);
        for (Profile profile: listProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(profile.getFullname());
            profileDto.setDescription(profile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
            profileDto.setDetail(profile.getDetail());
            profileDto.setId(profile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }
}
