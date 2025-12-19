package com.skillstracker.infrastructure.web.dto;

import java.time.LocalDate;
import java.util.UUID;

public class LearningSessionResponseDTO {

    private UUID id;
    private String skill;
    private LocalDate date;
    private Integer duration;
    private String notes;
    private String resources;
    private Integer totalDuration;

    public LearningSessionResponseDTO(UUID id, String skill, LocalDate date, Integer duration, String notes, String resources, Integer totalDuration) {
        this.id = id;
        this.skill = skill;
        this.date = date;
        this.duration = duration;
        this.notes = notes;
        this.resources = resources;
        this.totalDuration = totalDuration;
    }

    public UUID getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }

    public String getResources() {
        return resources;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }
}
