package com.rugby.parser;

import com.rugby.parser.domain.Game;
import com.rugby.parser.infrastructure.helpers.Tournament;
import com.rugby.parser.infrastructure.helpers.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

class UtilsTest {

    private final String[] text = "Jeudi 24 Novembre Carcassonne Pro D2 21h00 Agen Samedi 26 Novembre Montpellier Top 14 15h00 Bayonne Pays de Galles Autumn Nations Series 16h15 Australie Angleterre Autumn Nations Series 18h30 Afrique Du Sud Stade Francais Top 14 21h05 Toulon".split("\\s");

    @Test
    void getLastWordsAtTheBeginning() {
        Assertions.assertEquals("Jeudi 24 Novembre", Utils.getWordsAtIndex(text, 3, 3));
    }

    @Test
    void getLastWordsAtTheEnd() {
        Assertions.assertEquals("Stade Francais Top 14 21h05 Toulon", Utils.getWordsAtIndex(text, 38, 6));
    }

    @Test
    void getLastWordsInTheMiddle() {
        Assertions.assertEquals("Australie", Utils.getWordsAtIndex(text, 24, 1));
    }

    @Test
    void getWordsInEmptyText() {
        Assertions.assertEquals("", Utils.getWordsAtIndex("".split("\\s"), 114, 3));
    }

    @Test
    void getWordsInNullText() {
        Assertions.assertEquals("", Utils.getWordsAtIndex(null, 114, 3));
    }

    @Test
    void getWordsButIndexHigherThanWords() {
        Assertions.assertEquals("", Utils.getWordsAtIndex("Short Text".split("\\s"), 40, 3));
    }

    @Test
    void getMoreWordsThanWordsInText() {
        Assertions.assertEquals("", Utils.getWordsAtIndex("Short Text".split("\\s"), 4, 3));
    }
}
