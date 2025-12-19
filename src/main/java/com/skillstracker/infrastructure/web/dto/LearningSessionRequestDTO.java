package com.skillstracker.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class LearningSessionRequestDTO{
    private String skillName;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String notes;
    private String resourcesUsed;

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public LocalDateTime getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDateTime sessionDate) { this.sessionDate = sessionDate; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getResourcesUsed() { return resourcesUsed; }
    public void setResourcesUsed(String resourcesUsed) { this.resourcesUsed = resourcesUsed; }
}

