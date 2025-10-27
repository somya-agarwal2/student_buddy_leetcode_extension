package com.studentbuddy.repository;

import com.studentbuddy.model.ProblemNote;
import com.studentbuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemNoteRepository extends JpaRepository<ProblemNote, Long> {
    Optional<ProblemNote> findByProblemSlugAndUser(String problemSlug, User user);
    boolean existsByProblemSlugAndUser(String problemSlug, User user);
}
