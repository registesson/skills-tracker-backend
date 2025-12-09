package com.skillstracker.infrastructure.web.dto;

import com.skillstracker.domain.skill.SkillCategory;
import com.skillstracker.domain.skill.SkillLevel;

import java.time.LocalDateTime;
import java.util.UUID;

public record SkillDTO(UUID id,
                       String name,
                       String description,
                       SkillCategory category,
                       SkillLevel currentLevel,
                       SkillLevel targetLevel,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt,
                       Integer totalLearningSessions,
                       Long totalLearningHours) {
}
