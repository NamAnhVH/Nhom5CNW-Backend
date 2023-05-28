package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
//    ModelUser findByUsernameAndPassword(String username);

    Account findByUsername(String username);

    @Query("FROM Account a WHERE a.username = ?1 OR a.email = ?1")
    Account findByUsernameOrEmail(String usernameOrEmail);
    Account findByVerificationCode(String verificationCode);

    Account findByEmail(String email);

    List<Account> findByRoleNotAndVerifiedTrue(String role);

    List<Account> findByRoleAndVerifiedTrue(String role);

    List<Account> findByRoleNotAndApprovedFalseAndVerifiedTrue(String role);

    List<Account> findByRoleNotAndLockedTrueAndVerifiedTrue(String role);

    Account findByUsernameAndEmail(String username, String email);
}
