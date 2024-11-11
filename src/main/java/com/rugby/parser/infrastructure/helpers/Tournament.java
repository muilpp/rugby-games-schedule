package com.rugby.parser.infrastructure.helpers;

public enum Tournament {
    TOP_14("Top 14"),
    ANS("Autumn Nations Series"),
    CHAMPIONS("Champions Cup"),
    CHALLENGE_EUROPEEN("Challenge Europeen"),
    SIX_NATIONS("Six Nations"),
    THE_RUGBY_CHAMPIONSHIP("The Rugby Championship"),
    FRIENDLY("Amical"),
    TEST_MATCH("Test Match"),
    //WORLD_CUP("Coupe du Monde")
    ;
    //URC("United Rugby Championship"),
    //PREMIER("Gallagher Premiership");

    public final String value;

    Tournament(String value) {
        this.value = value;
    }
}
