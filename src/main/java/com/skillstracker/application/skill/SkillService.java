package com.skillstracker.application.skill;

import com.skillstracker.domain.skill.Skill;
import com.skillstracker.domain.skill.SkillCategory;
import com.skillstracker.domain.skill.SkillLevel;
import com.skillstracker.domain.user.User;
import com.skillstracker.infrastructure.persistence.SkillRepository;
import com.skillstracker.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public Skill createSkill(UUID userId, String name, SkillCategory category, SkillLevel currentLevel) {
        //User user = userRepository.findById(userId)
         //       .orElseThrow(() -> new RuntimeException("User not found"));
        User user = new User("temp","temp","temp", "");// Temporary hardcoded user
        userRepository.save(user); // Persister l'utilisateur d'abord
        Skill skill = new Skill(name, category, currentLevel, user);
        return skillRepository.save(skill);
    }

    public Skill updateSkillLevel(UUID skillId, SkillLevel newLevel) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        skill.updateLevel(newLevel);
        return skillRepository.save(skill);
    }

    public List<Skill> getUserSkills(UUID userId) {
        return skillRepository.findByUserId(userId);
    }

    public List<Skill> getUserSkillsByCategory(UUID userId, SkillCategory category) {
        return skillRepository.findByUserIdAndCategory(userId, category);
    }

    public void deleteSkill(UUID skillId) {
        skillRepository.deleteById(skillId);
    }
}
