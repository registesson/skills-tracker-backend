package com.skillstracker.infrastructure.persistence;

import com.skillstracker.domain.LearningSession.LearningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LearningSessionRepository extends JpaRepository<LearningSession, UUID> {
    List<LearningSession> findBySkillId(UUID skillId);

    @Query("SELECT ls FROM LearningSession ls WHERE ls.skill.id = :skillId ORDER BY ls.sessionDate DESC")
    List<LearningSession> findBySkillIdOrderBySessionDateDesc(UUID skillId);

    @Query("SELECT ls FROM LearningSession ls WHERE ls.skill.user.id = :userId AND ls.sessionDate BETWEEN :startDate AND :endDate")
    List<LearningSession> findByUserIdAndDateRange(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(ls.durationMinutes) FROM LearningSession ls WHERE ls.skill.id = :skillId")
    Long getTotalDurationBySkillId(UUID skillId);
}
