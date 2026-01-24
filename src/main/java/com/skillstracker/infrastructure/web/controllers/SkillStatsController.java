package com.skillstracker.infrastructure.web.controllers;

import com.skillstracker.application.skill.SkillStatsService;
import com.skillstracker.infrastructure.web.dto.SkillsStatsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/skills/{skillId}/stats")
public class SkillStatsController {
    private final SkillStatsService skillStatsService;

    public SkillStatsController(SkillStatsService skillStatsService) {
        this.skillStatsService = skillStatsService;
    }

    @GetMapping
    public ResponseEntity<SkillsStatsDto> getSkillStats(@PathVariable("skillId") UUID skillId) {
        UUID userId = getCurrentUserId(); // Implement this method to get the current user's ID
        return ResponseEntity.ok(skillStatsService.getSkillStats(skillId, userId));
    }

    private UUID getCurrentUserId() {
        return null;
    }
}
