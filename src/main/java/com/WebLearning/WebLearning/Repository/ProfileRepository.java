package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Account;
import com.WebLearning.WebLearning.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByAccountId(Long id);

}
