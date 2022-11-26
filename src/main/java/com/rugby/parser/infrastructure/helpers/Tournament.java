package com.rugby.parser.infrastructure.helpers;

public enum Tournament {
    TOP_14("Top 14"),
    ANS("Autumn Nations Series"),
    CHAMPIONS("Coupe d'Europe"),
    SIX_NATIONS("Six Nations");
    //URC("United Rugby Championship"),
    //PREMIER("Gallagher Premiership");

    public final String value;

    Tournament(String value) {
        this.value = value;
    }
}
