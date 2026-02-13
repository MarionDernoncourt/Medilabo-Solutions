package com.ms_reports.ms_reports.entity;

public enum RiskLevel {

    NONE("Aucun"),
    BORDERLINE("Risque limité"),
    IN_DANGER("En danger"),
    EARLY_ONSET("Apparition précoce"),
    NOT_APPLICABLE("Aucun");

    private final String label;

    RiskLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
