package com.studentbuddy.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteRequest {
    @NotBlank(message = "Problem slug is required")
    private String slug;

    @NotBlank(message = "Username is required")
    private String username;

    private String notes;

    // Constructors
    public NoteRequest() {}

    public NoteRequest(String slug, String username, String notes) {
        this.slug = slug;
        this.username = username;
        this.notes = notes;
    }

    // Getters and Setters
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
