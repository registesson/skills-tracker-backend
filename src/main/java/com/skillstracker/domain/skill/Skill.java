package com.skillstracker.domain.skill;

import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.domain.resource.LearningResource;
import com.skillstracker.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel currentLevel;

    @Enumerated(EnumType.STRING)
    private SkillLevel targetLevel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningSession> learningSessions = new ArrayList<>();

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningResource> resources = new ArrayList<>();

    protected Skill() {}

    public Skill(String name, SkillCategory category, SkillLevel currentLevel, User user) {
        this.name = name;
        this.category = category;
        this.currentLevel = currentLevel;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLevel(SkillLevel newLevel) {
        this.currentLevel = newLevel;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTargetLevel(SkillLevel targetLevel) {
        this.targetLevel = targetLevel;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public SkillCategory getCategory() { return category; }
    public SkillLevel getCurrentLevel() { return currentLevel; }
    public SkillLevel getTargetLevel() { return targetLevel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public User getUser() { return user; }
    public List<LearningSession> getLearningSessions() { return learningSessions; }
    public List<LearningResource> getResources() { return resources; }

}
