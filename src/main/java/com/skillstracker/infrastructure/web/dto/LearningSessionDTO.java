package com.skillstracker.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LearningSessionDTO(UUID id,
                                 UUID skillId,
                                 String skillName,
                                 LocalDateTime sessionDate,
                                 Integer durationMinutes,
                                 String notes,
                                 String resourcesUsed,
                                 LocalDateTime createdAt) {
}
