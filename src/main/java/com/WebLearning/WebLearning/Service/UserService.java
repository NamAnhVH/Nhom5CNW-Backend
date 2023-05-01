package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.ModelUser;
import com.WebLearning.WebLearning.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ModelUser findByUsernameAndPassword(String username, String password) {
        ModelUser user = userRepository.findByUsernameAndPassword(username, password);
        if(user != null) {
            return user;
        }
        return null;
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
}
