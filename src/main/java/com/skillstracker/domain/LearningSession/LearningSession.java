package com.skillstracker.domain.LearningSession;

import com.skillstracker.domain.skill.Skill;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_sessions")
public class LearningSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String resourcesUsed;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public LearningSession() {}

    public LearningSession(Skill skill, LocalDate sessionDate, Integer durationMinutes, String notes, String resourcesUsed) {
        this.skill = skill;
        this.sessionDate = sessionDate;
        this.durationMinutes = durationMinutes;
        this.createdAt = LocalDateTime.now();
        this.notes = notes;
        this.resourcesUsed = resourcesUsed;

    }

    public void addNotes(String notes) {
        this.notes = notes;
    }

    public void addResourcesUsed(String resources) {
        this.resourcesUsed = resources;
    }

    // Getters
    public UUID getId() { return id; }
    public Skill getSkill() { return skill; }
    public LocalDate getSessionDate() { return sessionDate; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getNotes() { return notes; }
    public String getResourcesUsed() { return resourcesUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setResourcesUsed(String resourcesUsed) {
        this.resourcesUsed = resourcesUsed;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
