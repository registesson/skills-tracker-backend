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

        Skill skill = resolveSkill(dto);

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
    public ResponseEntity<List<LearningSessionResponseDTO>> getAllSessions() {
        List<LearningSession> sessions = learningSessionService.getAllSessions();
        List<LearningSessionResponseDTO> responseDTOs = sessions.stream()
                .map(session -> new LearningSessionResponseDTO(
                        session.getId(),
                        session.getSkill().getName(),
                        session.getSessionDate(),
                        session.getDurationMinutes(),
                        session.getNotes(),
                        session.getResourcesUsed(),
                        0
                ))
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningSessionResponseDTO> updateSession(
            @PathVariable UUID id,
            @RequestBody LearningSessionRequestDTO dto) {

        LearningSession updated = learningSessionService.updateSession(id, dto);

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

    /**
     * Résout la Skill à partir du DTO : par skillId d'abord, sinon par skillName.
     */
    private Skill resolveSkill(LearningSessionRequestDTO dto) {
        if (dto.getSkillId() != null) {
            return skillRepository.findById(dto.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + dto.getSkillId()));
        }
        if (dto.getSkillName() != null) {
            Skill skill = skillRepository.findSkillByName(dto.getSkillName());
            if (skill == null) {
                throw new IllegalArgumentException("Skill not found with name: " + dto.getSkillName());
            }
            return skill;
        }
        throw new IllegalArgumentException("Either skillId or skillName must be provided");
    }
}
