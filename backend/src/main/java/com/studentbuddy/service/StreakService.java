package com.studentbuddy.service;

import com.studentbuddy.dto.StreakUpdateRequest;
import com.studentbuddy.model.Streak;
import com.studentbuddy.model.User;
import com.studentbuddy.repository.StreakRepository;
import com.studentbuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class StreakService {

    @Autowired
    private StreakRepository streakRepository;

    @Autowired
    private UserRepository userRepository;

    public Streak getStreak(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return streakRepository.findByUser(user)
                .orElseGet(() -> {
                    Streak newStreak = new Streak(user);
                    return streakRepository.save(newStreak);
                });
    }

    public Streak updateStreak(StreakUpdateRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Streak streak = streakRepository.findByUser(user)
                .orElseGet(() -> {
                    Streak newStreak = new Streak(user);
                    return streakRepository.save(newStreak);
                });

        LocalDate today = LocalDate.now();
        LocalDate lastSolvedDate = streak.getLastSolvedDate();

        if (lastSolvedDate == null) {
            // First problem solved
            streak.setCurrentStreak(1);
            streak.setLongestStreak(1);
            streak.setLastSolvedDate(today);
            streak.setTotalProblemsSolved(1);
        } else if (lastSolvedDate.equals(today)) {
            // Already solved a problem today, just increment total
            streak.setTotalProblemsSolved(streak.getTotalProblemsSolved() + 1);
        } else if (lastSolvedDate.equals(today.minusDays(1))) {
            // Consecutive day, increment streak
            streak.setCurrentStreak(streak.getCurrentStreak() + 1);
            streak.setLongestStreak(Math.max(streak.getCurrentStreak(), streak.getLongestStreak()));
            streak.setLastSolvedDate(today);
            streak.setTotalProblemsSolved(streak.getTotalProblemsSolved() + 1);
        } else {
            // Streak broken, reset to 1
            streak.setCurrentStreak(1);
            streak.setLastSolvedDate(today);
            streak.setTotalProblemsSolved(streak.getTotalProblemsSolved() + 1);
        }

        return streakRepository.save(streak);
    }
}
