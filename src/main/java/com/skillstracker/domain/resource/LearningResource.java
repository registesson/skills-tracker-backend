package com.skillstracker.domain.resource;

import com.skillstracker.domain.skill.Skill;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    private String url;

    @Column(length = 500)
    private String description;

    private Boolean completed = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected LearningResource() {}

    public LearningResource(Skill skill, String title, ResourceType type, String url) {
        this.skill = skill;
        this.title = title;
        this.type = type;
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    // Getters
    public UUID getId() { return id; }
    public Skill getSkill() { return skill; }
    public String getTitle() { return title; }
    public ResourceType getType() { return type; }
    public String getUrl() { return url; }
    public String getDescription() { return description; }
    public Boolean getCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

}
