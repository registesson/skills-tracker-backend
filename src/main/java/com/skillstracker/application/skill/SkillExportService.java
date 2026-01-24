package com.skillstracker.application.skill;

import com.skillstracker.domain.LearningSession.LearningSession;
import com.skillstracker.domain.skill.Skill;
import com.skillstracker.infrastructure.persistence.SkillRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class SkillExportService {

    private final SkillRepository skillRepository;

    public SkillExportService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Exporte les compétences d'un utilisateur au format CSV
     * @param userId L'identifiant de l'utilisateur
     * @return Le contenu CSV sous forme de tableau d'octets
     */
    public byte[] exportSkillsToCsv(UUID userId) {
        List<Skill> skills = skillRepository.findByUserId(userId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osw)) {

            // En-têtes CSV
            writer.println("nom,categorie,niveau,temps_total_minutes");

            // Données
            for (Skill skill : skills) {
                long totalMinutes = skill.getLearningSessions().stream()
                        .mapToLong(LearningSession::getDurationMinutes)
                        .sum();

                writer.printf("%s,%s,%s,%d%n",
                        escapeCsvValue(skill.getName()),
                        skill.getCategory(),
                        skill.getCurrentLevel(),
                        totalMinutes
                );
            }

            writer.flush();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier CSV", e);
        }
    }

    /**
     * Échappe les valeurs CSV pour gérer les virgules, guillemets et retours à la ligne
     */
    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }

        // Si la valeur contient une virgule, un guillemet ou un retour à la ligne,
        // on l'entoure de guillemets et on double les guillemets internes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}

