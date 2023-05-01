package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.ModelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ModelUser, Long> {
    ModelUser findByUsernameAndPassword(String username, String password);

    ModelUser findByUsername(String username);

}
