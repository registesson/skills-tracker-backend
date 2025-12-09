package com.skillstracker.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateLearningSessionRequest(
        @NotNull(message = "Skill ID is required")
        UUID skillId,

        @NotNull(message = "Session date is required")
        LocalDateTime sessionDate,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        Integer durationMinutes,

        String notes,
        String resourcesUsed
) {
}
