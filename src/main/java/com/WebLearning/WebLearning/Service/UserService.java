package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.ModelUser;
import com.WebLearning.WebLearning.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ModelUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isExistedAccount(String username){
        ModelUser user = userRepository.findByUsername(username);
        if(user != null){
            return true;
        }
        return false;
    }

    public ModelUser findById(Long id) {
        return userRepository.findById(id).get();
    }

    public ModelUser save(ModelUser user) {
        return userRepository.saveAndFlush(user);
    }

    public List<ModelUser> findAll(){
        List<ModelUser> listUser = userRepository.findAll();
        return listUser;
    }

    public void add(ModelUser modelUser) {
        ModelUser newUser = new ModelUser();
        newUser.setFullname(modelUser.getFullname());
        newUser.setUsername(modelUser.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder().encode(modelUser.getPassword()));
        newUser.setRole(modelUser.getRole());
        if(newUser.getRole().equals("hoc-sinh")){
            newUser.setApproved(true);
        }
        else {
            newUser.setApproved(false);
        }
        userRepository.saveAndFlush(newUser);
    }

    public List<ModelUser> findAccountByOption(String option) {
        List<ModelUser> listAccount = new ArrayList<>();
        if (option.equals("studentAccount")){
            listAccount = userRepository.findByRole("hoc-sinh");
        } else if (option.equals("teacherAccount")) {
            listAccount = userRepository.findByRole("giao-vien");
        } else if (option.equals("unapprovedAccount")) {
            listAccount = userRepository.findByApprovedFalse();
        } else if (option.equals("lockedAccount")) {
            listAccount = userRepository.findByLockedTrue();
        }
        return listAccount;
    }

    public List<ModelUser> findByRoleNot(String role) {
        return userRepository.findByRoleNot(role);
    }

    public void approveAccount(Long id) {
        ModelUser user = userRepository.findById(id).get();
        user.setApproved(true);
        userRepository.save(user);
    }

    public void lockAccount(Long id) {
        ModelUser user = userRepository.findById(id).get();
        user.setLocked(true);
        userRepository.save(user);
    }

    public void unlockAccount(Long id) {
        ModelUser user = userRepository.findById(id).get();
        user.setLocked(false);
        userRepository.save(user);
    }
}
