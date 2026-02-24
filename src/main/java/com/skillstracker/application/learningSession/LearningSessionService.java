package com.skillstracker.application.learningSession;

import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.infrastructure.persistence.LearningSessionRepository;
import com.skillstracker.infrastructure.web.dto.LearningSessionRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LearningSessionService {
    private final LearningSessionRepository repository;

    public LearningSessionService(LearningSessionRepository repository) {
        this.repository = repository;
    }

    public LearningSession createSession(LearningSession session) {
        return repository.save(session);
    }

    public List<LearningSession> getAllSessions() {
        return repository.findAll();
    }

    public List<LearningSession> getSessionsBySkillId(UUID skillId) {
        return repository.findBySkillId(skillId);
    }

    public Integer getTotalDurationBySkillId(UUID skillId) {
        Long total = repository.getTotalDurationBySkillId(skillId);
        return total != null ? total.intValue() : 0;
    }

    public LearningSession updateSession(UUID id, LearningSessionRequestDTO dto) {
        LearningSession existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Learning session not found with id: " + id));

        if (dto.getSessionDate() != null) {
            existing.setSessionDate(dto.getSessionDate().toLocalDate());
        }
        if (dto.getDurationMinutes() != null) {
            existing.setDurationMinutes(dto.getDurationMinutes());
        }
        existing.setNotes(dto.getNotes());
        existing.setResourcesUsed(dto.getResourcesUsed());

        return repository.save(existing);
    }

    public void deleteSession(UUID id) {
        repository.deleteById(id);
    }
}
