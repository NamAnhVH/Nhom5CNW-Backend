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
public class AccountService {

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

    public void addAccount(UserRegistrationDto newUser) {
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

        Profile profile = new Profile();
        profile.setFullname(newUser.getFullname());
        profile.setAccount(newAccount);
        profileRepository.save(profile);
    }

    public List<Account> getAccountByOption(String option) {
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

    public List<Account> getAccountByRoleNot(String role) {
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

        Profile profile = profileRepository.findByAccountId(account.getId());
        profile.setFullname(newUser.getFullname());
        profileRepository.save(profile);
    }

    public void replaceAccountByEmail(UserRegistrationDto newUser) {
        Account account = accountRepository.findByEmail(newUser.getEmail());
        Account deleteAccount = accountRepository.findByUsername(newUser.getUsername());
        if(deleteAccount != null){
            Profile deleteProfile = profileRepository.findByAccountId(deleteAccount.getId());
            profileRepository.deleteById(deleteProfile.getId());
            accountRepository.deleteById(deleteAccount.getId());
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

        Profile profile = profileRepository.findByAccountId(account.getId());
        profile.setFullname(newUser.getFullname());
        profileRepository.save(profile);
    }


//    public void saveAvatar(MultipartFile file, ModelUser user) throws IOException {
////        ModelUser user = userRepository.findById((long) 4).get();
//        user.setAvatar(file.getBytes());
//        userRepository.save(user);
//    }
}
