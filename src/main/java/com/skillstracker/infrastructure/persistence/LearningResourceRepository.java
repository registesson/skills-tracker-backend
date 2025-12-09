package com.skillstracker.infrastructure.persistence;

import com.skillstracker.domain.resource.LearningResource;
import com.skillstracker.domain.resource.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, UUID> {
    List<LearningResource> findBySkillId(UUID skillId);

    List<LearningResource> findBySkillIdAndCompleted(UUID skillId, Boolean completed);

    List<LearningResource> findBySkillIdAndType(UUID skillId, ResourceType type);
}
