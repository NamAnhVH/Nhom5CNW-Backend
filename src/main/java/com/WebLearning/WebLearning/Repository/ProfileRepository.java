package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByAccountId(Long id);

    List<Profile> findByAccountRoleOrderByIdDesc(String role);

    List<Profile> findTop6ByAccountRoleOrderByIdDesc(String role);

    List<Profile> findTop6ByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(String role);

    List<Profile> findByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(String role);
}
