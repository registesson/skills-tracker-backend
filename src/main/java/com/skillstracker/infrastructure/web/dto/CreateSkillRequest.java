package com.skillstracker.infrastructure.web.dto;

import com.skillstracker.domain.skill.SkillCategory;
import com.skillstracker.domain.skill.SkillLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSkillRequest(
        @NotBlank(message = "Skill name is required")
        String name,

        String description,

        @NotNull(message = "Category is required")
        SkillCategory category,

        @NotNull(message = "Current level is required")
        SkillLevel currentLevel,

        SkillLevel targetLevel
) {
}
