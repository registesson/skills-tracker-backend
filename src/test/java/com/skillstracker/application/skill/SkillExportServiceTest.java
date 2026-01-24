package com.skillstracker.application.skill;

import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.domain.skill.Skill;
import com.skillstracker.domain.skill.SkillCategory;
import com.skillstracker.domain.skill.SkillLevel;
import com.skillstracker.domain.user.User;
import com.skillstracker.infrastructure.persistence.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillExportServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillExportService skillExportService;

    private UUID userId;
    private User user;
    private List<Skill> skills;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .password("password")
                .build();

        skills = new ArrayList<>();

        // Skill 1: Java avec 2 sessions d'apprentissage
        Skill javaSkill = new Skill("Java", SkillCategory.PROGRAMMING, SkillLevel.INTERMEDIATE, user);
        LearningSession session1 = new LearningSession(javaSkill, LocalDate.now(), 60, "Notes", "Resources");
        LearningSession session2 = new LearningSession(javaSkill, LocalDate.now().minusDays(1), 90, "Notes", "Resources");
        javaSkill.getLearningSessions().add(session1);
        javaSkill.getLearningSessions().add(session2);
        skills.add(javaSkill);

        // Skill 2: React sans sessions
        Skill reactSkill = new Skill("React", SkillCategory.FRAMEWORK, SkillLevel.BEGINNER, user);
        skills.add(reactSkill);

        // Skill 3: avec une virgule dans le nom pour tester l'échappement CSV
        Skill specialSkill = new Skill("Spring Boot, Framework", SkillCategory.FRAMEWORK, SkillLevel.ADVANCED, user);
        LearningSession session3 = new LearningSession(specialSkill, LocalDate.now(), 120, "Notes", "Resources");
        specialSkill.getLearningSessions().add(session3);
        skills.add(specialSkill);
    }

    @Test
    void exportSkillsToCsv_shouldGenerateValidCsv() {
        // Given
        when(skillRepository.findByUserId(userId)).thenReturn(skills);

        // When
        byte[] csvData = skillExportService.exportSkillsToCsv(userId);
        String csvContent = new String(csvData, StandardCharsets.UTF_8);

        // Then
        assertThat(csvContent).isNotEmpty();

        String[] lines = csvContent.split("\n");
        assertThat(lines).hasSizeGreaterThanOrEqualTo(4); // Header + 3 skills

        // Vérifier l'en-tête
        assertThat(lines[0]).contains("nom,categorie,niveau,temps_total_minutes");

        // Vérifier que les compétences sont présentes
        assertThat(csvContent).contains("Java,PROGRAMMING,INTERMEDIATE,150"); // 60 + 90 minutes
        assertThat(csvContent).contains("React,FRAMEWORK,BEGINNER,0");
        assertThat(csvContent).contains("\"Spring Boot, Framework\",FRAMEWORK,ADVANCED,120"); // Nom échappé
    }

    @Test
    void exportSkillsToCsv_shouldHandleEmptySkillsList() {
        // Given
        when(skillRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        // When
        byte[] csvData = skillExportService.exportSkillsToCsv(userId);
        String csvContent = new String(csvData, StandardCharsets.UTF_8);

        // Then
        assertThat(csvContent).isNotEmpty();
        String[] lines = csvContent.split("\n");
        assertThat(lines).hasSize(1); // Only header
        assertThat(lines[0]).contains("nom,categorie,niveau,temps_total_minutes");
    }

    @Test
    void exportSkillsToCsv_shouldCalculateTotalTimeCorrectly() {
        // Given
        when(skillRepository.findByUserId(userId)).thenReturn(skills);

        // When
        byte[] csvData = skillExportService.exportSkillsToCsv(userId);
        String csvContent = new String(csvData, StandardCharsets.UTF_8);

        // Then
        // Java skill should have 150 minutes total (60 + 90)
        assertThat(csvContent).contains("Java,PROGRAMMING,INTERMEDIATE,150");

        // React skill should have 0 minutes (no sessions)
        assertThat(csvContent).contains("React,FRAMEWORK,BEGINNER,0");

        // Spring Boot skill should have 120 minutes
        assertThat(csvContent).contains("ADVANCED,120");
    }
}

