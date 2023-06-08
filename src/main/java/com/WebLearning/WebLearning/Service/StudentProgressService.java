package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.StudentProgressDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Models.StudentProgress;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Repository.StudentProgressRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentProgressService {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    @Autowired
    private StudentProgressRepository studentProgressRepository;


    public List<StudentProgressDto> getByCurrentAccount() {
        Account account = authenticationFacade.getAccount();
        StudentProfile student = studentProfileRepository.findByAccountId(account.getId());
        List<StudentProgress> listProgress = studentProgressRepository.findByStudentId(student.getId());
        List<StudentProgressDto> listProgressDto = new ArrayList<>();
        for(StudentProgress progress: listProgress){
            StudentProgressDto progressDto = new StudentProgressDto();
            progressDto.setCourse(progress.getLecture().getCourse().getName());
            progressDto.setLecture(progress.getLecture().getTitle());
            progressDto.setNumber(progress.getNumber());
            progressDto.setGrade(progress.getGrade());
            progressDto.setTime(formatLocalDateTimeToString(progress.getTime()));
            listProgressDto.add(progressDto);
        }
        return listProgressDto;
    }

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }
}
