package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.User;
import com.WebLearning.WebLearning.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if(user != null) {
            return user;
        }
        return null;
    }

    public boolean isExistedAccount(String username){
        User user = userRepository.findByUsername(username);
        if(user != null){
            return true;
        }
        return false;
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }
}
