package com.rugby.parser.application;

import com.rugby.parser.domain.Game;
import com.rugby.parser.domain.ports.GameService;
import com.rugby.parser.infrastructure.helpers.DaysOfWeek;
import com.rugby.parser.infrastructure.helpers.Tournament;
import com.rugby.parser.infrastructure.helpers.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static com.rugby.parser.infrastructure.helpers.Utils.formatFrenchDate;
import static com.rugby.parser.infrastructure.helpers.Utils.formatUkDate;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private static final String FRENCH_GAMES_URL = "https://www.agendatv-rugby.com/";
    private static final String FRENCH_GAME_TIME_PATTER = "\\d?\\d[h]\\d{2}";
    private static final String UK_GAMES_URL = "https://www.rugbyontv.co.uk/rugby-union-on-tv.php";
    private static final String UK_GAME_TIME_PATTER = "\\d?\\d[:]\\d{2}\\s(PM|AM)?";

    @Override
    public Map<LocalDate, List<String>> getGames(int maxDays) {
        Map<LocalDate, List<Game>> frenchGamesMap = getFrenchGames(maxDays);

        Map<LocalDate, List<Game>> ukGamesMap = getUkGames(maxDays);
        Map<LocalDate, List<Game>> gamesMap = new TreeMap<>(frenchGamesMap);

        ukGamesMap.forEach((key, value) -> gamesMap.merge(key, value, (uk, france) -> {
            List<Game> gameList = new ArrayList<>();
            gameList.addAll(france);
            gameList.addAll(uk);
            gameList.sort(Comparator.comparing(Game::getTime));

            return gameList;
        }));

        Map<LocalDate, List<String>> games = new TreeMap<>();
        for (Map.Entry<LocalDate,List<Game>> entry : gamesMap.entrySet()) {
            games.put(entry.getKey(), entry.getValue().stream().map(g -> g.toString()).collect(Collectors.toList()));
        }

        gamesMap.forEach((k,v) -> System.out.println(k +", " + gamesMap.get(k)));

        return games;
    }

    private Map<LocalDate, List<Game>> getFrenchGames(int maxDays) {
        Document doc;
        try {
            doc = Jsoup.connect(FRENCH_GAMES_URL).get();
        } catch (IOException e) {
            log.error("Not possible to get french games", e);
            return Collections.emptyMap();
        }

        String htmlText = doc.select("div[class=col-12] > div.container-fluid").stream().filter(Element::hasText).map(Element::text).reduce("", (a, b) -> a+b);

        htmlText = htmlText.replace("Aujourd'hui", CaseUtils.toCamelCase(LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH), true) + " " + LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH));
        htmlText = htmlText.replace("Demain", CaseUtils.toCamelCase(LocalDate.now().plusDays(1).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH), true) + " " + LocalDate.now().plusDays(1).getDayOfMonth() + " " + LocalDate.now().plusDays(1).getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH));
        String[] htmlTextArray = htmlText.split("(?="+ DaysOfWeek.LUNDI.value + "|" +DaysOfWeek.MARDI.value + "|" +DaysOfWeek.MERCREDI.value + "|" +DaysOfWeek.JEUDI.value + "|" +DaysOfWeek.VENDREDI.value + "|" +DaysOfWeek.SAMEDI.value + "|" +DaysOfWeek.DIMANCHE.value +")");

        Map<LocalDate, List<Game>> gameMap = new TreeMap<>();

        for (String element : htmlTextArray) {
            String[] allWords = element.split("\\s");
            for ( int i = 0; i < allWords.length; i++ ) {
                Game game = createFrenchGame(allWords, i);
                if (game == null) continue;

                LocalDate gameDate = formatFrenchDate(allWords[1]+" "+allWords[2]);
                if (gameDate.isBefore(LocalDate.now().plusDays(maxDays))) {
                    if (gameMap.containsKey(gameDate)) {
                        gameMap.get(gameDate).add(game);
                    } else {
                        gameMap.put(gameDate, new ArrayList<>(List.of(game)));
                    }
                }
            }
        }

        return gameMap;
    }

    private Game createFrenchGame(String[] allWords, int index) {
        if (Arrays.stream(Tournament.values()).anyMatch(tournament -> tournament.value.startsWith(allWords[index]))) {
            String tournament = Arrays.stream(Tournament.values()).filter(t -> t.value.startsWith(allWords[index])).map(t -> t.value).findFirst().orElse("");

            String homeTeam = Utils.getWordsAtIndex(allWords, index, 1);
            if (homeTeam.length() <= 2)
                homeTeam = Utils.getWordsAtIndex(allWords, index, 2);

            String awayTeam = Utils.getWordsAtIndex(allWords, index+tournament.split("\\s").length+2, 1);
            if (awayTeam.length() <= 2)
                awayTeam = Utils.getWordsAtIndex(allWords, index+tournament.split("\\s").length+3, 2);

            String gameTime = Utils.getWordsAtIndex(allWords, index+tournament.split("\\s").length+1, 1);
            if (!gameTime.matches(FRENCH_GAME_TIME_PATTER))
                return null;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'h'mm");
            LocalTime time = LocalTime.parse(gameTime, formatter);

            return new Game(homeTeam, awayTeam, time, tournament);
        }

        return null;
    }

    private Map<LocalDate, List<Game>> getUkGames(int maxDays) {
        Document doc;
        try {
            doc = Jsoup.connect(UK_GAMES_URL).get();
        } catch (IOException e) {
            log.error("Not possible to get UK games", e);
            return Collections.emptyMap();
        }

        List<String> htmlTextList = doc.select("div[class=wrap]").stream().filter(Element::hasText).map(Element::text).filter(text -> !text.contains("No Rugby Union fixtures")).collect(Collectors.toList());
        Map<LocalDate, List<Game>> gameMap = new TreeMap<>();

        for (String htmlText : htmlTextList) {
            htmlText = htmlText.replace("Today -", "").trim();
            String[] htmlTextArray = htmlText.split("\\s");

            LocalDate gameDate = formatUkDate(htmlTextArray[1] + " " + htmlTextArray[3]);
            if (gameDate.isAfter(LocalDate.now().plusDays(maxDays))) continue;

            List<Game> gameList = new ArrayList<>();

            for (int i = 0; i < htmlTextArray.length; i++) {
                Game game = createUkGame(htmlTextArray, i);
                if (game != null)
                    gameList.add(game);
            }

            gameMap.put(gameDate, gameList);
        }

        return gameMap;
    }

    private Game createUkGame(String[] htmlTextArray, int index) {
        if (Arrays.stream(Tournament.values()).anyMatch(t -> t.value.split("\\s")[0].equalsIgnoreCase(htmlTextArray[index]))) {
            String tournament = Arrays.stream(Tournament.values()).filter(t -> t.value.startsWith(htmlTextArray[index])).map(t -> t.value).findFirst().orElse("");
            if (tournament.contains(Tournament.TOP_14.value))
                return null;

            String gameTime = Utils.getWordsAtIndex(htmlTextArray, index, 1).replace("pm", " PM");
            String game = Utils.getWordsAtIndex(htmlTextArray, index-1, 3);
            if (!gameTime.matches(UK_GAME_TIME_PATTER)) {
                //if the game is today, the css shows the game time after the tournament
                gameTime = Utils.getWordsAtIndex(htmlTextArray, index+tournament.split("\\s").length+1, 1).replace("pm", " PM");
                if (!gameTime.matches(UK_GAME_TIME_PATTER)) return null;
                game = Utils.getWordsAtIndex(htmlTextArray, index, 3);
            }

            LocalTime time = formatUkGameTime(gameTime);

            if (game.startsWith("v"))
                game = Utils.getWordsAtIndex(htmlTextArray, index-1, 4);
            String homeTeam = game.split(" v ")[0].trim();
            String awayTeam = game.split(" v ")[1].trim();
            return new Game(homeTeam, awayTeam, time.plusHours(1), tournament);
        }

        return null;
    }

    private LocalTime formatUkGameTime(String gameTime) {
        gameTime = gameTime.length() == 8 ? gameTime : "0"+gameTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return LocalTime.parse(gameTime, formatter);
    }
}
