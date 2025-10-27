package com.studentbuddy.controller;

import com.studentbuddy.model.ProblemHint;
import com.studentbuddy.service.HintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hints")
@CrossOrigin(origins = "*")
public class HintController {

    @Autowired
    private HintService hintService;

    @GetMapping("/{slug}")
    public ResponseEntity<?> getHints(@PathVariable String slug) {
        try {
            ProblemHint hint = hintService.getHints(slug);
            return ResponseEntity.ok(hint);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveHints(@RequestBody HintRequest request) {
        try {
            ProblemHint hint = hintService.saveHints(
                request.getSlug(), 
                request.getHints(), 
                request.getDifficulty(), 
                request.getTitle()
            );
            return ResponseEntity.ok(hint);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private static class HintRequest {
        private String slug;
        private String hints;
        private String difficulty;
        private String title;

        // Getters and Setters
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getHints() { return hints; }
        public void setHints(String hints) { this.hints = hints; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
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
