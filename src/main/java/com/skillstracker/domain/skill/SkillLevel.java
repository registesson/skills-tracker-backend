package com.skillstracker.domain.skill;

public enum SkillLevel {
    BEGINNER(1, "Débutant"),
    ELEMENTARY(2, "Élémentaire"),
    INTERMEDIATE(3, "Intermédiaire"),
    ADVANCED(4, "Avancé"),
    EXPERT(5, "Expert");

    private final int value;
    private final String label;

    SkillLevel(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() { return value; }
    public String getLabel() { return label; }

    public static SkillLevel fromValue(int value) {
        for (SkillLevel level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid skill level: " + value);
    }
}
