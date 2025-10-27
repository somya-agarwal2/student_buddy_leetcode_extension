package com.studentbuddy.dto;

import jakarta.validation.constraints.NotBlank;

public class TemplateRequest {
    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotBlank(message = "Template content is required")
    private String templateContent;

    private String username;

    // Constructors
    public TemplateRequest() {}

    public TemplateRequest(String language, String templateName, String templateContent, String username) {
        this.language = language;
        this.templateName = templateName;
        this.templateContent = templateContent;
        this.username = username;
    }

    // Getters and Setters
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
