package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    List<Reference> findByAccountId(Long id);

    List<Reference> findTop6ByAccountLockedFalseOrderByTimeDesc();

    List<Reference> findByAccountLockedFalseOrderByTimeDesc();

    void deleteByAccountId(Long id);
}
