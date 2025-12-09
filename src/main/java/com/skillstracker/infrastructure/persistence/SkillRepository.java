package com.skillstracker.infrastructure.persistence;

import com.skillstracker.domain.skill.Skill;
import com.skillstracker.domain.skill.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    List<Skill> findByUserId(UUID userId);

    List<Skill> findByUserIdAndCategory(UUID userId, SkillCategory category);

    @Query("SELECT s FROM Skill s WHERE s.user.id = :userId ORDER BY s.updatedAt DESC")
    List<Skill> findByUserIdOrderByUpdatedAtDesc(UUID userId);

    @Query("SELECT COUNT(s) FROM Skill s WHERE s.user.id = :userId")
    long countByUserId(UUID userId);
}
