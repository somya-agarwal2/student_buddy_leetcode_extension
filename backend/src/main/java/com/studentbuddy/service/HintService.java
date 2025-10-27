package com.studentbuddy.service;

import com.studentbuddy.model.ProblemHint;
import com.studentbuddy.repository.ProblemHintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HintService {

    @Autowired
    private ProblemHintRepository hintRepository;

    public ProblemHint getHints(String slug) {
        return hintRepository.findByProblemSlug(slug)
                .orElseGet(() -> {
                    ProblemHint newHint = new ProblemHint();
                    newHint.setProblemSlug(slug);
                    newHint.setHints("No hints available for this problem.");
                    newHint.setCreatedAt(LocalDateTime.now());
                    newHint.setUpdatedAt(LocalDateTime.now());
                    return newHint;
                });
    }

    public ProblemHint saveHints(String slug, String hints, String difficulty, String title) {
        ProblemHint hint = hintRepository.findByProblemSlug(slug)
                .orElseGet(() -> {
                    ProblemHint newHint = new ProblemHint();
                    newHint.setProblemSlug(slug);
                    newHint.setCreatedAt(LocalDateTime.now());
                    return newHint;
                });

        hint.setHints(hints);
        hint.setDifficulty(difficulty);
        hint.setTitle(title);
        hint.setUpdatedAt(LocalDateTime.now());

        return hintRepository.save(hint);
    }
}
