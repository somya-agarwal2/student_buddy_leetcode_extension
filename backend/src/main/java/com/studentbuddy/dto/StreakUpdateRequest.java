package com.studentbuddy.dto;

import jakarta.validation.constraints.NotBlank;

public class StreakUpdateRequest {
    @NotBlank(message = "Username is required")
    private String username;

    // Constructors
    public StreakUpdateRequest() {}

    public StreakUpdateRequest(String username) {
        this.username = username;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
