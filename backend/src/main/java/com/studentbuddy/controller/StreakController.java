package com.studentbuddy.controller;

import com.studentbuddy.dto.StreakUpdateRequest;
import com.studentbuddy.model.Streak;
import com.studentbuddy.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streak")
@CrossOrigin(origins = "*")
public class StreakController {

    @Autowired
    private StreakService streakService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getStreak(@PathVariable String username) {
        try {
            Streak streak = streakService.getStreak(username);
            return ResponseEntity.ok(streak);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateStreak(@RequestBody StreakUpdateRequest request) {
        try {
            Streak streak = streakService.updateStreak(request);
            return ResponseEntity.ok(streak);
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
