package com.skillstracker.infrastructure.web.controllers;

import com.skillstracker.application.learningSession.LearningSessionService;
import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.domain.skill.Skill;
import com.skillstracker.infrastructure.persistence.SkillRepository;
import com.skillstracker.infrastructure.web.dto.LearningSessionRequestDTO;
import com.skillstracker.infrastructure.web.dto.LearningSessionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning-sessions")
public class LearningSessionController {

    private final LearningSessionService learningSessionService;
    private final SkillRepository skillRepository;

    public LearningSessionController(LearningSessionService learningSessionService, SkillRepository skillRepository) {
        this.learningSessionService = learningSessionService;
        this.skillRepository = skillRepository;
    }

    @PostMapping
    public ResponseEntity<LearningSessionResponseDTO> createSession(@RequestBody LearningSessionRequestDTO dto) {


        Skill skill = skillRepository.findSkillByName(dto.getSkillName());

        LearningSession session = new LearningSession(
                skill,
                dto.getSessionDate() != null ? dto.getSessionDate().toLocalDate() : null,
                dto.getDurationMinutes(),
                dto.getNotes(),
                dto.getResourcesUsed()
        );

        LearningSession saved = learningSessionService.createSession(session);

        return new ResponseEntity<>(
                new LearningSessionResponseDTO(
                        saved.getId(),
                        saved.getSkill().getName(),
                        saved.getSessionDate(),
                        saved.getDurationMinutes(),
                        saved.getNotes(),
                        saved.getResourcesUsed(),
                        0
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<LearningSession>> getAllSessions() {
        return ResponseEntity.ok(learningSessionService.getAllSessions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningSessionResponseDTO> updateSession(
            @PathVariable UUID id,
            @RequestBody LearningSessionRequestDTO dto) {

        LearningSession session = new LearningSession(
                null,
                dto.getSessionDate().toLocalDate(),
                dto.getDurationMinutes(),
                dto.getNotes(),
                dto.getResourcesUsed()
        );

        LearningSession updated = learningSessionService.updateSession(id, session);

        return ResponseEntity.ok(
                new LearningSessionResponseDTO(
                        updated.getId(),
                        updated.getSkill().getName(),
                        updated.getSessionDate(),
                        updated.getDurationMinutes(),
                        updated.getNotes(),
                        updated.getResourcesUsed(),
                        0
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        learningSessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }



}
