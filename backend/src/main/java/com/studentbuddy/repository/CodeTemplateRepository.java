package com.studentbuddy.repository;

import com.studentbuddy.model.CodeTemplate;
import com.studentbuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeTemplateRepository extends JpaRepository<CodeTemplate, Long> {
    List<CodeTemplate> findByLanguageAndIsDefaultTrue(String language);
    List<CodeTemplate> findByLanguageAndUser(String language, User user);
    Optional<CodeTemplate> findByLanguageAndTemplateNameAndUser(String language, String templateName, User user);
}
