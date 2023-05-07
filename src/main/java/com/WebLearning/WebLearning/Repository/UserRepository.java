package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.ModelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<ModelUser, Long> {
//    ModelUser findByUsernameAndPassword(String username);

    ModelUser findByUsername(String username);

    List<ModelUser> findByRole(String role);

    List<ModelUser> findByApprovedFalse();

    List<ModelUser> findByLockedTrue();
    List<ModelUser> findByRoleNot(String role);
}
