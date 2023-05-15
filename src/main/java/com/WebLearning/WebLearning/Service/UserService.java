package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Profile;
import com.WebLearning.WebLearning.Repository.AccountRepository;
import com.WebLearning.WebLearning.Repository.ProfileRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import com.WebLearning.WebLearning.FormData.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean isExistedAccount(String username){
        Account user = accountRepository.findByUsername(username);
        if(user != null){
            return true;
        }
        return false;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    public Account save(Account user) {
        return accountRepository.saveAndFlush(user);
    }

    public List<Account> findAll(){
        List<Account> listUser = accountRepository.findAll();
        return listUser;
    }

    public void add(UserRegistrationDto newUser) {
        Account newAccount = new Account();
        newAccount.setUsername(newUser.getUsername());
        newAccount.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        newAccount.setRole(newUser.getRole());
        if(newAccount.getRole().equals("học sinh")){
            newAccount.setApproved(true);
        }
        else {
            newAccount.setApproved(false);
        }
        accountRepository.save(newAccount);

        Profile profile = new Profile();
        profile.setFullname(newUser.getFullname());
        profile.setAccount(newAccount);
        profileRepository.save(profile);
    }

    public List<Account> findAccountByOption(String option) {
        List<Account> listAccount = new ArrayList<>();
        if (option.equals("studentAccount")){
            listAccount = accountRepository.findByRole("học sinh");
        } else if (option.equals("teacherAccount")) {
            listAccount = accountRepository.findByRole("giáo viên");
        } else if (option.equals("unapprovedAccount")) {
            listAccount = accountRepository.findByApprovedFalse();
        } else if (option.equals("lockedAccount")) {
            listAccount = accountRepository.findByLockedTrue();
        }
        return listAccount;
    }

    public List<Account> findAccountByRoleNot(String role) {
        return accountRepository.findByRoleNot(role);
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

    public String getFullname() {
        Account currentAccount = authenticationFacade.getAccount();
        Profile profile = profileRepository.findByAccountId(currentAccount.getId());
        return profile.getFullname();
    }

    public List<Profile> findAllProfile() {
        return profileRepository.findAll();
    }


//    public void saveAvatar(MultipartFile file, ModelUser user) throws IOException {
////        ModelUser user = userRepository.findById((long) 4).get();
//        user.setAvatar(file.getBytes());
//        userRepository.save(user);
//    }
}
