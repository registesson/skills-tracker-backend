package com.skillstracker.infrastructure.web.dto;

import com.skillstracker.domain.skill.SkillLevel;
import jakarta.validation.constraints.NotNull;

public record UpdateSkillLevelRequest(@NotNull(message = "New level is required")
                                      SkillLevel newLevel) {
}
