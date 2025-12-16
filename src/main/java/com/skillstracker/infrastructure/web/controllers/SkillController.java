package com.skillstracker.infrastructure.web.controllers;

import com.skillstracker.application.skill.SkillService;
import com.skillstracker.domain.skill.Skill;
import com.skillstracker.domain.skill.SkillCategory;
import com.skillstracker.infrastructure.web.dto.CreateSkillRequest;
import com.skillstracker.infrastructure.web.dto.SkillDTO;
import com.skillstracker.infrastructure.web.dto.UpdateSkillLevelRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Skills", description = "Skill management endpoints")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody CreateSkillRequest request, Authentication authentication) {
        //UUID userId = UUID.fromString(authentication.getName());
        UUID userId = UUID.fromString("1eaac572-fa52-4b2d-a4f3-27af3a18e97a"); // Temporary hardcoded user ID
        Skill skill = skillService.createSkill(userId, request.name(), request.category(), request.currentLevel());

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(skill));
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getUserSkills(Authentication authentication) {
        //UUID userId = UUID.fromString(authentication.getName());
        UUID userId = UUID.fromString("1eaac572-fa52-4b2d-a4f3-27af3a18e97a"); // Temporary hardcoded user ID
        List<Skill> skills = skillService.getUserSkills(userId);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(skillDTOs);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SkillDTO>> getUserSkillsByCategory(Authentication authentication, @PathVariable SkillCategory category) {

        UUID userId = UUID.fromString(authentication.getName());

        List<Skill> skills = skillService.getUserSkillsByCategory(userId, category);
        List<SkillDTO> skillDTOs = skills.stream()
                .map(this::mapToDTO)
                .toList();

        return ResponseEntity.ok(skillDTOs);
    }

    @PutMapping("/{skillId}/level")
    public ResponseEntity<SkillDTO> updateSkillLevel(@PathVariable UUID skillId, @Valid @RequestBody UpdateSkillLevelRequest request) {

        Skill updatedSkill = skillService.updateSkillLevel(skillId, request.newLevel());

        return ResponseEntity.ok(mapToDTO(updatedSkill));
    }

    @DeleteMapping("/{skillId}")
    @Operation(summary = "Delete a skill", description = "Delete a specific skill")
    @SecurityRequirement(name = "bearer-jwt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Skill deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Skill not found")
    })
    public ResponseEntity<Void> deleteSkill(@PathVariable UUID skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }

    private @Nullable SkillDTO mapToDTO(Skill skill) {
        return new SkillDTO(
                skill.getId(),
                skill.getName(),
                skill.getDescription(),
                skill.getCategory(),
                skill.getCurrentLevel(),
                skill.getTargetLevel(),
                skill.getCreatedAt(),
                skill.getUpdatedAt(),
                skill.getLearningSessions().size(),
                skill.getLearningSessions().stream()
                        .mapToLong(ls -> ls.getDurationMinutes())
                        .sum() / 60
        );
    }
}
