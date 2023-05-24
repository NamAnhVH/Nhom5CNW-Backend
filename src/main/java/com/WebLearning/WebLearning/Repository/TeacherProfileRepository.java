package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {

    TeacherProfile findByAccountId(Long id);

    List<TeacherProfile> findTop6ByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(String role);

    List<TeacherProfile> findByAccountRoleAndAccountApprovedTrueAndAccountLockedFalseOrderByIdDesc(String role);
}
