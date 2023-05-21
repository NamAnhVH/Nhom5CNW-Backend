package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    ModelUser findByUsernameAndPassword(String username);

    Account findByUsername(String username);

    List<Account> findByRole(String role);

    List<Account> findByApprovedFalse();

    List<Account> findByLockedTrue();
    List<Account> findByRoleNot(String role);

    Account findByVerificationCode(String verificationCode);

    Account findByEmail(String email);
}
