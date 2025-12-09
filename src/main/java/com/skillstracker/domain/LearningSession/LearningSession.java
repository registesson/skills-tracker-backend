package com.skillstracker.domain.LearningSession;

import com.skillstracker.domain.skill.Skill;
import jakarta.persistence.*;

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
    private LocalDateTime sessionDate;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String resourcesUsed;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected LearningSession() {}

    public LearningSession(Skill skill, LocalDateTime sessionDate, Integer durationMinutes) {
        this.skill = skill;
        this.sessionDate = sessionDate;
        this.durationMinutes = durationMinutes;
        this.createdAt = LocalDateTime.now();
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
    public LocalDateTime getSessionDate() { return sessionDate; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getNotes() { return notes; }
    public String getResourcesUsed() { return resourcesUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
