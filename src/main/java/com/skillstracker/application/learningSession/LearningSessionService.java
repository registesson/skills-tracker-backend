package com.skillstracker.application.learningSession;

import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.infrastructure.persistence.LearningSessionRepository;
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

    public LearningSession updateSession(UUID id, LearningSession session) {
        session.setId(id);
        return repository.save(session);
    }

    public void deleteSession(UUID id) {
        repository.deleteById(id);
    }
}
