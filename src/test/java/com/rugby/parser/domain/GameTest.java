package com.rugby.parser.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class GameTest {

    @Test
    void gameStringBuiltCorrectly() {
        Game game = new Game("Usap", "Leinster", LocalTime.of(20, 0), "Cope d'Europe");
        Assertions.assertEquals("Usap vs Leinster at 20:00 in Cope d'Europe", game.toString());
    }
}
