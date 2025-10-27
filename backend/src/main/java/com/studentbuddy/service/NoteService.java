package com.studentbuddy.service;

import com.studentbuddy.dto.NoteRequest;
import com.studentbuddy.dto.NoteShareRequest;
import com.studentbuddy.model.ProblemNote;
import com.studentbuddy.model.User;
import com.studentbuddy.repository.ProblemNoteRepository;
import com.studentbuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteService {

    @Autowired
    private ProblemNoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public ProblemNote getNotes(String slug, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository.findByProblemSlugAndUser(slug, user)
                .orElseGet(() -> {
                    ProblemNote newNote = new ProblemNote();
                    newNote.setProblemSlug(slug);
                    newNote.setNotes("");
                    newNote.setUser(user);
                    newNote.setCreatedAt(LocalDateTime.now());
                    newNote.setUpdatedAt(LocalDateTime.now());
                    return newNote;
                });
    }

    public ProblemNote saveNotes(NoteRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProblemNote note = noteRepository.findByProblemSlugAndUser(request.getSlug(), user)
                .orElseGet(() -> {
                    ProblemNote newNote = new ProblemNote();
                    newNote.setProblemSlug(request.getSlug());
                    newNote.setUser(user);
                    newNote.setCreatedAt(LocalDateTime.now());
                    return newNote;
                });

        note.setNotes(request.getNotes());
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public ProblemNote shareNotes(NoteShareRequest request) {
        User fromUser = userRepository.findByUsername(request.getFromUsername())
                .orElseThrow(() -> new RuntimeException("From user not found"));

        User toUser = userRepository.findByUsername(request.getToUsername())
                .orElseThrow(() -> new RuntimeException("To user not found"));

        // Create or update note for the recipient
        ProblemNote sharedNote = noteRepository.findByProblemSlugAndUser(request.getSlug(), toUser)
                .orElseGet(() -> {
                    ProblemNote newNote = new ProblemNote();
                    newNote.setProblemSlug(request.getSlug());
                    newNote.setUser(toUser);
                    newNote.setCreatedAt(LocalDateTime.now());
                    return newNote;
                });

        sharedNote.setNotes(request.getNotes());
        sharedNote.setIsShared(true);
        sharedNote.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(sharedNote);
    }
}
