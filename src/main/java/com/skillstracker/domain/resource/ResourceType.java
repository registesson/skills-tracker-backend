package com.skillstracker.domain.resource;

public enum ResourceType {
    BOOK("Livre"),
    ONLINE_COURSE("Cours en ligne"),
    DOCUMENTATION("Documentation"),
    VIDEO("Vidéo"),
    ARTICLE("Article"),
    TUTORIAL("Tutoriel"),
    EXERCISE("Exercice"),
    PROJECT("Projet"),
    OTHER("Autre");

    private final String label;

    ResourceType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }
}
