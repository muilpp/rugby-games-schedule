package com.rugby.parser.infrastructure.helpers;

public enum DaysOfWeek {
    LUNDI("Lundi"),
    MARDI("Mardi"),
    MERCREDI("Mercredi"),
    JEUDI("Jeudi"),
    VENDREDI("Vendredi"),
    SAMEDI("Samedi"),
    DIMANCHE("Dimanche");

    public final String value;

    DaysOfWeek(String value) {
        this.value = value;
    }
}
