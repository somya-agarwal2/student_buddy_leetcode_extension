package com.studentbuddy.repository;

import com.studentbuddy.model.Streak;
import com.studentbuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Long> {
    Optional<Streak> findByUser(User user);
    boolean existsByUser(User user);
}
