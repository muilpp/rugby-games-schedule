package com.rugby.parser.domain.ports;

import com.rugby.parser.domain.Game;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameService {

    Map<LocalDate, List<String>> getGames(int maxDays);
}
