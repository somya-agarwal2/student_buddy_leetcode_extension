package com.studentbuddy.repository;

import com.studentbuddy.model.ProblemHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemHintRepository extends JpaRepository<ProblemHint, Long> {
    Optional<ProblemHint> findByProblemSlug(String problemSlug);
    boolean existsByProblemSlug(String problemSlug);
}
