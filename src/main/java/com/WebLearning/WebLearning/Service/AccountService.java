package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.ForgetPasswordDto;
import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.StudentProfile;
import com.WebLearning.WebLearning.Models.TeacherProfile;
import com.WebLearning.WebLearning.Repository.*;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.FormData.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private CourseCommentRepository courseCommentRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Transactional
    public void deleteStudentAccount() {
        Account account = authenticationFacade.getAccount();
        StudentProfile studentProfile = studentProfileRepository.findByAccountId(account.getId());
        courseCommentRepository.deleteByStudentProfileId(studentProfile.getId());
        referenceRepository.deleteByAccountId(account.getId());
        studentProfile.getCourses().clear();
        studentProfileRepository.delete(studentProfile);
        accountRepository.deleteById(account.getId());
    }

    @Transactional
    public void deleteTeacherAccount() {
        Account account = authenticationFacade.getAccount();
        TeacherProfile teacherProfile = teacherProfileRepository.findByAccountId(account.getId());
        List<Course> listCourse = courseRepository.findByTeacherId(teacherProfile.getId());
        for(Course course : listCourse){
            courseCommentRepository.deleteByCourseId(course.getId());
            courseRepository.delete(course);
        }
        referenceRepository.deleteByAccountId(account.getId());
        teacherProfileRepository.delete(teacherProfile);
        accountRepository.deleteById(account.getId());
    }


    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean isExistedAccount(String username){
        Account account = accountRepository.findByUsername(username);
        if(account != null){
            return true;
        }
        return false;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    public Account save(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public List<Account> findAll(){
        List<Account> listAccount = accountRepository.findAll();
        return listAccount;
    }

    public void addAccount(UserRegistrationDto newUser) throws IOException {
        Account newAccount = new Account();
        newAccount.setUsername(newUser.getUsername());
        newAccount.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        newAccount.setEmail(newUser.getEmail());
        newAccount.setRole(newUser.getRole());
        if(newAccount.getRole().equals("học sinh")){
            newAccount.setApproved(true);
        }
        else {
            newAccount.setApproved(false);
        }
        accountRepository.save(newAccount);

        if(newAccount.getRole().equals("học sinh")){
            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setFullname(newUser.getFullname());
            studentProfile.setAvatar(readFileToBytes(new File("src/main/resources/static/image/standard-avatar.jpg")));
            studentProfile.setAccount(newAccount);
            studentProfileRepository.save(studentProfile);
        } else {
            TeacherProfile teacherProfile = new TeacherProfile();
            teacherProfile.setFullname(newUser.getFullname());
            teacherProfile.setAvatar(readFileToBytes(new File("src/main/resources/static/image/standard-avatar.jpg")));
            teacherProfile.setAccount(newAccount);
            teacherProfileRepository.save(teacherProfile);
        }
    }

    public List<Account> getAccountByOptionAndVerified(String option) {
        List<Account> listAccount = new ArrayList<>();
        if (option.equals("studentAccount")){
            listAccount = accountRepository.findByRoleAndVerifiedTrue("học sinh");
        } else if (option.equals("teacherAccount")) {
            listAccount = accountRepository.findByRoleAndVerifiedTrue("giáo viên");
        } else if (option.equals("unapprovedAccount")) {
            listAccount = accountRepository.findByRoleNotAndApprovedFalseAndVerifiedTrue("admin");
        } else if (option.equals("lockedAccount")) {
            listAccount = accountRepository.findByRoleNotAndLockedTrueAndVerifiedTrue("admin");
        }
        return listAccount;
    }

    public List<Account> getAccountByRoleNotAndVerified(String role) {
        return accountRepository.findByRoleNotAndVerifiedTrue(role);
    }

    public void approveAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setApproved(true);
        accountRepository.save(account);
    }

    public void lockAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setLocked(true);
        accountRepository.save(account);
    }

    public void unlockAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setLocked(false);
        accountRepository.save(account);
    }

    public boolean isExistedEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if(account != null){
            return true;
        }
        return false;
    }

    public boolean isVerifiedByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        return account.isVerified();
    }

    public boolean isVerifiedByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        return account.isVerified();
    }

    public void replaceAccountByUsername(UserRegistrationDto newUser) {
        Account account = accountRepository.findByUsername(newUser.getUsername());
        Account deleteAccount = accountRepository.findByUsername(newUser.getUsername());
        if(deleteAccount != null){
            if(deleteAccount.getRole().equals("học sinh")){
                StudentProfile deleteStudentProfile = studentProfileRepository.findByAccountId(deleteAccount.getId());
                studentProfileRepository.deleteById(deleteStudentProfile.getId());
                accountRepository.deleteById(deleteAccount.getId());
            } else {
                TeacherProfile deleteTeacherProfile = teacherProfileRepository.findByAccountId(deleteAccount.getId());
                teacherProfileRepository.deleteById(deleteTeacherProfile.getId());
                accountRepository.deleteById(deleteAccount.getId());
            }
        }
        account.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        account.setEmail(newUser.getEmail());
        account.setRole(newUser.getRole());
        if(account.getRole().equals("học sinh")){
            account.setApproved(true);
        }
        else {
            account.setApproved(false);
        }
        accountRepository.save(account);
        account = accountRepository.findByUsername(newUser.getUsername());
        if(account.getRole().equals("học sinh")){
            StudentProfile studentProfile = studentProfileRepository.findByAccountId(account.getId());
            if(studentProfile == null){
                studentProfile = new StudentProfile();
                studentProfile.setAccount(account);
            }
            studentProfile.setFullname(newUser.getFullname());
            studentProfileRepository.save(studentProfile);
        } else {
            TeacherProfile teacherProfile = teacherProfileRepository.findByAccountId(account.getId());
            if(teacherProfile == null){
                teacherProfile = new TeacherProfile();
                teacherProfile.setAccount(account);
            }
            teacherProfile.setFullname(newUser.getFullname());
            teacherProfileRepository.save(teacherProfile);
        }
    }

    public void replaceAccountByEmail(UserRegistrationDto newUser) {
        Account account = accountRepository.findByEmail(newUser.getEmail());
        Account deleteAccount = accountRepository.findByUsername(newUser.getUsername());
        if(deleteAccount != null){
            if(deleteAccount.getRole().equals("học sinh")){
                StudentProfile deleteStudentProfile = studentProfileRepository.findByAccountId(deleteAccount.getId());
                studentProfileRepository.deleteById(deleteStudentProfile.getId());
                accountRepository.deleteById(deleteAccount.getId());
            } else {
                TeacherProfile deleteTeacherProfile = teacherProfileRepository.findByAccountId(deleteAccount.getId());
                teacherProfileRepository.deleteById(deleteTeacherProfile.getId());
                accountRepository.deleteById(deleteAccount.getId());
            }
        }
        account.setUsername(newUser.getUsername());
        account.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        account.setRole(newUser.getRole());
        if(account.getRole().equals("học sinh")){
            account.setApproved(true);
        }
        else {
            account.setApproved(false);
        }
        accountRepository.save(account);
        account = accountRepository.findByUsername(newUser.getUsername());
        if(account.getRole().equals("học sinh")){
            StudentProfile studentProfile = studentProfileRepository.findByAccountId(account.getId());
            if(studentProfile == null){
                studentProfile = new StudentProfile();
                studentProfile.setAccount(account);
            }
            studentProfile.setFullname(newUser.getFullname());
            studentProfileRepository.save(studentProfile);
        } else {
            TeacherProfile teacherProfile = teacherProfileRepository.findByAccountId(account.getId());
            if(teacherProfile == null){
                teacherProfile = new TeacherProfile();
                teacherProfile.setAccount(account);
            }
            teacherProfile.setFullname(newUser.getFullname());
            teacherProfileRepository.save(teacherProfile);
        }
    }


    public String getEmailCurrentAccount() {
        Account currentAccount = accountRepository.findByUsername(authenticationFacade.getAccount().getUsername());
        return currentAccount.getEmail();
    }

    public Account getCurrentAccount() {
        return authenticationFacade.getAccount();
    }

    public boolean isAuthenticated() {
        return authenticationFacade.isAuthenticated();
    }

    public void login(Account user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void changeEmail(String verificationCode) {
        Account account = accountRepository.findByVerificationCode(verificationCode);
        account.setEmail(authenticationFacade.getAccount().getEmail());
        accountRepository.save(account);
    }

    public boolean checkPassword(String checkPassword) {
        String encodePassword = authenticationFacade.getAccount().getPassword();
        return new BCryptPasswordEncoder().matches(checkPassword,encodePassword);
    }

    public void setNewPassword(String newPassword) {
        Account currentAccount = accountRepository.findByUsername(authenticationFacade.getAccount().getUsername());
        currentAccount.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        authenticationFacade.getAccount().setPassword(currentAccount.getPassword());
        accountRepository.save(currentAccount);
    }

    public boolean checkAccount(ForgetPasswordDto forgetPasswordDto) {
        Account account = accountRepository.findByUsernameAndEmail(forgetPasswordDto.getUsername(),forgetPasswordDto.getEmail());
        if(account != null){
            return true;
        }
        return false;
    }

    public void setNewPassword(String newPassword, String code) {
        Account account = accountRepository.findByVerificationCode(code);
        account.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        accountRepository.save(account);
    }

    public byte[] readFileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }
}
