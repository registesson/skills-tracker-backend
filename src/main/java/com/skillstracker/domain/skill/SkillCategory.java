package com.skillstracker.domain.skill;

public enum SkillCategory {

    PROGRAMMING("Programmation"),
    FRAMEWORK("Framework"),
    DATABASE("Base de données"),
    DEVOPS("DevOps"),
    ARCHITECTURE("Architecture"),
    SOFT_SKILLS("Soft Skills"),
    TOOLS("Outils"),
    LANGUAGE("Langue"),
    OTHER("Autre");

    private final String label;

    SkillCategory(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }
}
