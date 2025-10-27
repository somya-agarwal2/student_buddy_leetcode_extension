package com.studentbuddy.controller;

import com.studentbuddy.dto.NoteRequest;
import com.studentbuddy.dto.NoteShareRequest;
import com.studentbuddy.model.ProblemNote;
import com.studentbuddy.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/{slug}/{username}")
    public ResponseEntity<?> getNotes(@PathVariable String slug, @PathVariable String username) {
        try {
            ProblemNote note = noteService.getNotes(slug, username);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveNotes(@Valid @RequestBody NoteRequest request) {
        try {
            ProblemNote note = noteService.saveNotes(request);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/share")
    public ResponseEntity<?> shareNotes(@Valid @RequestBody NoteShareRequest request) {
        try {
            ProblemNote note = noteService.shareNotes(request);
            return ResponseEntity.ok(note);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
