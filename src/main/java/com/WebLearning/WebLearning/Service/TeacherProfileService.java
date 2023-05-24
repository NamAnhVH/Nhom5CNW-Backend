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
        TeacherProfile currentTeacherProfile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(currentTeacherProfile.getFullname());
        profileDto.setDescription(currentTeacherProfile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(currentTeacherProfile.getAvatar()));
        profileDto.setDetail(currentTeacherProfile.getDetail());
        return profileDto;
    }

    public void updateProfile(TeacherProfileDto profile) throws IOException {
        Account currentAccount = authenticationFacade.getAccount();
        TeacherProfile currentTeacherProfile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        currentTeacherProfile.setFullname(profile.getFullname());
        if(!profile.getAvatar().isEmpty()){
            currentTeacherProfile.setAvatar(profile.getAvatar().getBytes());
        }
        currentTeacherProfile.setDescription(profile.getDescription());
        currentTeacherProfile.setDetail(profile.getDetail());
        teacherProfileRepository.save(currentTeacherProfile);
    }

    public TeacherProfileDto getById(Long id) {
        TeacherProfile teacherProfile = teacherProfileRepository.findById(id).get();
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(teacherProfile.getFullname());
        profileDto.setDescription(teacherProfile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(teacherProfile.getAvatar()));
        profileDto.setDetail(teacherProfile.getDetail());
        return profileDto;
    }

    public List<TeacherProfileDto> getTopSixTeacherApprovedAndUnlocked() {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<TeacherProfile> listTeacherProfile = teacherProfileRepository.findTop6ByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc("giáo viên");
        for (TeacherProfile teacherProfile : listTeacherProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(teacherProfile.getFullname());
            profileDto.setDescription(teacherProfile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(teacherProfile.getAvatar()));
            profileDto.setId(teacherProfile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }

    public List<TeacherProfileDto> getAllByRoleAndApprovedAndUnlocked(String role) {
        List<TeacherProfileDto> listProfileDto = new ArrayList<>();
        List<TeacherProfile> listTeacherProfile = teacherProfileRepository.findByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(role);
        for (TeacherProfile teacherProfile : listTeacherProfile) {
            TeacherProfileDto profileDto = new TeacherProfileDto();
            profileDto.setFullname(teacherProfile.getFullname());
            profileDto.setDescription(teacherProfile.getDescription());
            profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(teacherProfile.getAvatar()));
            profileDto.setId(teacherProfile.getId());
            listProfileDto.add(profileDto);
        }
        return listProfileDto;
    }

    public String getFullname() {
        Account currentAccount = authenticationFacade.getAccount();
        TeacherProfile teacherProfile = teacherProfileRepository.findByAccountId(currentAccount.getId());
        return teacherProfile.getFullname();
    }

    public List<TeacherProfile> getAllProfile() {
        return teacherProfileRepository.findAll();
    }

    public boolean isApprovedAndUnlocked(Long id) {
        TeacherProfile teacherProfile = teacherProfileRepository.findById(id).get();
        if(teacherProfile.getAccount().isApproved() && !teacherProfile.getAccount().isLocked()){
            return true;
        }
        return false;
    }

    public TeacherProfileDto getAllProfileTeachCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        TeacherProfile teacherProfile = course.getTeacher();
        TeacherProfileDto profileDto = new TeacherProfileDto();
        profileDto.setFullname(teacherProfile.getFullname());
        profileDto.setDescription(teacherProfile.getDescription());
        profileDto.setBase64Avatar("data:image/png;base64," + Base64.encodeBase64String(teacherProfile.getAvatar()));
        profileDto.setId(teacherProfile.getId());
        return profileDto;
    }
}
