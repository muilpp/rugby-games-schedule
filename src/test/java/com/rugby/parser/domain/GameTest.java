package com.rugby.parser.domain;

import com.rugby.parser.infrastructure.helpers.Tournament;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;

class GameTest {

    @Test
    void gameStringBuiltCorrectly() {
        Game game = new Game("Usap", "Leinster", LocalTime.of(20, 0), "Cope d'Europe");
        Assertions.assertEquals("Usap vs Leinster at 20:00 in Cope d'Europe", game.toString());
    }

    //@Test
    void noDuplicateGamesFoundInASet() {
        //given four games
        Game game1 = new Game("Wales", "Australia", LocalTime.of(16, 15), Tournament.ANS.value);
        Game game2 = new Game("Galles", "Australie", LocalTime.of(16, 15), Tournament.ANS.value);
        Game game3 = new Game("England", "South Africa", LocalTime.of(18, 30), Tournament.ANS.value);
        Game game4 = new Game("Anglaterre", "Afrique", LocalTime.of(18, 30), Tournament.ANS.value);
        Game game5 = new Game("Anglaterre", "Afrique", LocalTime.of(18, 30), Tournament.SIX_NATIONS.value);
        Game game6 = new Game("Anglaterre", "Afrique", LocalTime.of(17, 30), Tournament.ANS.value);
        Game game7 = new Game("New Zealand", "France", LocalTime.of(18, 30), Tournament.ANS.value);

        //when games are added to a set
        Set<Game> gameSet = new TreeSet<>();
        gameSet.add(game1);
        gameSet.add(game3);
        gameSet.add(game2);
        gameSet.add(game4);
        gameSet.add(game5);
        gameSet.add(game6);
        gameSet.add(game7);
/*        if ((this.getTime().compareTo(otherGame.getTime()) == 0) &&
            (this.getTournament().equalsIgnoreCase(otherGame.getTournament()))) {
            return true;
        }*/
        //only non duplicates will be found
        Assertions.assertEquals(3, gameSet.size());
        Game firstGame = gameSet.stream().findFirst().orElse(new Game());
        Game secondGame = gameSet.stream().skip(1).findFirst().orElse(new Game());
        Game thirdGame = gameSet.stream().skip(2).findFirst().orElse(new Game());
        Assertions.assertEquals("Wales vs Australia at 16:15 in Autumn Nations Series", firstGame.toString());
        Assertions.assertEquals("England vs South Africa at 18:30 in Autumn Nations Series", secondGame.toString());
        Assertions.assertEquals("New Zealand vs France at 18:30 in Autumn Nations Series", thirdGame.toString());
    }
}
