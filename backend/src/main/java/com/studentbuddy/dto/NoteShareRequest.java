package com.studentbuddy.dto;

import jakarta.validation.constraints.NotBlank;

public class NoteShareRequest {
    @NotBlank(message = "Problem slug is required")
    private String slug;

    @NotBlank(message = "From username is required")
    private String fromUsername;

    @NotBlank(message = "To username is required")
    private String toUsername;

    @NotBlank(message = "Notes are required")
    private String notes;

    // Constructors
    public NoteShareRequest() {}

    public NoteShareRequest(String slug, String fromUsername, String toUsername, String notes) {
        this.slug = slug;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.notes = notes;
    }

    // Getters and Setters
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
