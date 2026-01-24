package com.skillstracker.application.skill;

import com.skillstracker.domain.skill.Skill;
import com.skillstracker.infrastructure.persistence.LearningSessionRepository;
import com.skillstracker.infrastructure.persistence.SkillRepository;
import com.skillstracker.infrastructure.web.dto.SkillsStatsDto;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillStatsService {
    private final SkillRepository skillRepository;
    private final LearningSessionRepository learningSessionRepository;

    public SkillStatsService(SkillRepository skillRepository, LearningSessionRepository learningSessionRepository) {
        this.skillRepository = skillRepository;
        this.learningSessionRepository = learningSessionRepository;
    }

    public SkillsStatsDto getSkillStats(UUID skillId, UUID userId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        var sessions = learningSessionRepository.findBySkillIdAndUserId(skillId, userId);
        int totalSessions = sessions.size();

        Integer totalMinutes = learningSessionRepository.getTotalMinutesBySkillAndUser(skillId, userId);
        double totalHoursInvested = (totalMinutes != null ? totalMinutes : 0) / 60.0;

        var levelProgressions = sessions.stream()
                .map(s -> new SkillsStatsDto.LevelProgression(s.getCreatedAt(), s.getDurationMinutes().toString()))
                .collect(Collectors.toList());

        return new SkillsStatsDto(skillId, skill.getName(), totalSessions, totalHoursInvested, levelProgressions);
    }


}
