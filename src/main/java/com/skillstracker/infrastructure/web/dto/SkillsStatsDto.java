package com.skillstracker.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SkillsStatsDto {

    private UUID skillId;
    private String skillName;
    private int totalSessions;
    private double totalHoursInvested;
    private List<LevelProgression> levelProgressions;

    public static class LevelProgression {
        private LocalDateTime date;
        private String level;

        public LevelProgression(LocalDateTime date, String level) {
            this.date = date;
            this.level = level;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public SkillsStatsDto(UUID skillId, String skillName, int totalSessions, double totalHoursInvested, List<LevelProgression> levelProgressions) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.totalSessions = totalSessions;
        this.totalHoursInvested = totalHoursInvested;
        this.levelProgressions = levelProgressions;
    }

    public UUID getSkillId() {
        return skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public double getTotalHoursInvested() {
        return totalHoursInvested;
    }

    public void setTotalHoursInvested(double totalHoursInvested) {
        this.totalHoursInvested = totalHoursInvested;
    }

    public List<LevelProgression> getLevelProgressions() {
        return levelProgressions;
    }

    public void setLevelProgressions(List<LevelProgression> levelProgressions) {
        this.levelProgressions = levelProgressions;
    }
}
