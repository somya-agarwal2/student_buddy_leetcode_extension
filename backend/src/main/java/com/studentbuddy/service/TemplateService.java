package com.studentbuddy.service;

import com.studentbuddy.dto.TemplateRequest;
import com.studentbuddy.model.CodeTemplate;
import com.studentbuddy.model.User;
import com.studentbuddy.repository.CodeTemplateRepository;
import com.studentbuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private CodeTemplateRepository templateRepository;

    @Autowired
    private UserRepository userRepository;

    public CodeTemplate getTemplate(String language, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // First try to get user's custom template
        List<CodeTemplate> userTemplates = templateRepository.findByLanguageAndUser(language, user);
        if (!userTemplates.isEmpty()) {
            return userTemplates.get(0);
        }

        // Fallback to default template
        List<CodeTemplate> defaultTemplates = templateRepository.findByLanguageAndIsDefaultTrue(language);
        if (!defaultTemplates.isEmpty()) {
            return defaultTemplates.get(0);
        }

        // If no template exists, create a default one
        return createDefaultTemplate(language);
    }

    public CodeTemplate saveTemplate(TemplateRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CodeTemplate template = templateRepository.findByLanguageAndTemplateNameAndUser(
                request.getLanguage(), request.getTemplateName(), user)
                .orElseGet(() -> {
                    CodeTemplate newTemplate = new CodeTemplate();
                    newTemplate.setLanguage(request.getLanguage());
                    newTemplate.setTemplateName(request.getTemplateName());
                    newTemplate.setUser(user);
                    newTemplate.setCreatedAt(LocalDateTime.now());
                    return newTemplate;
                });

        template.setTemplateContent(request.getTemplateContent());
        template.setUpdatedAt(LocalDateTime.now());

        return templateRepository.save(template);
    }

    private CodeTemplate createDefaultTemplate(String language) {
        String templateContent = getDefaultTemplateContent(language);
        String templateName = "Default " + language.toUpperCase() + " Template";

        CodeTemplate template = new CodeTemplate(language, templateName, templateContent);
        template.setIsDefault(true);
        template = templateRepository.save(template);

        return template;
    }

    private String getDefaultTemplateContent(String language) {
        switch (language.toLowerCase()) {
            case "java":
                return "public class Solution {\n" +
                       "    public int[] twoSum(int[] nums, int target) {\n" +
                       "        // Your code here\n" +
                       "        return new int[0];\n" +
                       "    }\n" +
                       "}";
            case "python":
                return "class Solution:\n" +
                       "    def twoSum(self, nums: List[int], target: int) -> List[int]:\n" +
                       "        # Your code here\n" +
                       "        return []";
            case "cpp":
                return "class Solution {\n" +
                       "public:\n" +
                       "    vector<int> twoSum(vector<int>& nums, int target) {\n" +
                       "        // Your code here\n" +
                       "        return {};\n" +
                       "    }\n" +
                       "};";
            default:
                return "// Default template for " + language;
        }
    }
}
