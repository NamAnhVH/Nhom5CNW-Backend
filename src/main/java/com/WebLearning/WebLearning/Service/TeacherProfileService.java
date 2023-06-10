package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.TeacherProfileDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.TeacherProfile;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.TeacherProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherProfileService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;
    @Autowired
    private CourseRepository courseRepository;

    public TeacherProfileDto getCurrentAccountProfile() {
        Account currentAccount = authenticationFacade.getAccount();
        TeacherProfile profile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(profile.getFullname());
        profileDto.setDescription(profile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
        profileDto.setDetail(profile.getDetail());
        return profileDto;
    }

    public void updateProfile(TeacherProfileDto profileDto) throws IOException {
        Account currentAccount = authenticationFacade.getAccount();
        TeacherProfile profile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        profile.setFullname(profileDto.getFullname());
        if(!profileDto.getAvatar().isEmpty()){
            profile.setAvatar(profileDto.getAvatar().getBytes());
        }
        profile.setDescription(profileDto.getDescription());
        profile.setDetail(profileDto.getDetail());
        teacherProfileRepository.save(profile);
    }

    public TeacherProfileDto getById(Long id) {
        TeacherProfile profile = teacherProfileRepository.findById(id).get();
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(profile.getFullname());
        profileDto.setDescription(profile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
        profileDto.setDetail(profile.getDetail());
        return profileDto;
    }

    public List<TeacherProfileDto> getTopSixTeacherApprovedAndUnlocked() {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<TeacherProfile> listProfile = teacherProfileRepository.findTop6ByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc("giáo viên");
        for (TeacherProfile profile : listProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(profile.getFullname());
            profileDto.setDescription(profile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
            profileDto.setId(profile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }

    public List<TeacherProfileDto> getAllByRoleAndApprovedAndUnlocked(String role) {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<TeacherProfile> listProfile = teacherProfileRepository.findByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(role);
        for (TeacherProfile profile : listProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(profile.getFullname());
            profileDto.setDescription(profile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
            profileDto.setId(profile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }

    public String getFullname() {
        Account currentAccount = authenticationFacade.getAccount();
        TeacherProfile profile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        return profile.getFullname();
    }

    public List<TeacherProfile> getAllProfile() {
        return teacherProfileRepository.findAll();
    }

    public boolean isApprovedAndUnlocked(Long id) {
        TeacherProfile profile = teacherProfileRepository.findById(id).get();
        if(profile.getAccount().isApproved() && !profile.getAccount().isLocked()){
            return true;
        }
        return false;
    }

    public TeacherProfileDto getAllProfileTeachCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        TeacherProfile profile = course.getTeacher();
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(profile.getFullname());
        profileDto.setDescription(profile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
        profileDto.setId(profile.getId());
        return profileDto;
    }

    public TeacherProfileDto getFullnameAndAvatar() {
        TeacherProfileDto profileDto = new TeacherProfileDto();
        TeacherProfile profile = teacherProfileRepository.findByAccountId(authenticationFacade.getAccount().getId());
        profileDto.setFullname(profile.getFullname());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(profile.getAvatar()));
        return  profileDto;
    }
}
