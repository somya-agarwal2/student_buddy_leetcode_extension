package com.studentbuddy.controller;

import com.studentbuddy.dto.TemplateRequest;
import com.studentbuddy.model.CodeTemplate;
import com.studentbuddy.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("/{language}")
    public ResponseEntity<?> getTemplate(@PathVariable String language, @RequestParam String username) {
        try {
            CodeTemplate template = templateService.getTemplate(language, username);
            return ResponseEntity.ok(template);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> saveTemplate(@Valid @RequestBody TemplateRequest request) {
        try {
            CodeTemplate template = templateService.saveTemplate(request);
            return ResponseEntity.ok(template);
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
