package com.rugby.parser.infrastructure;

import com.rugby.parser.domain.ports.GameService;
import com.rugby.parser.domain.ports.MessageProducer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class Init {

    private final GameService gameService;
    private final MessageProducer messageProducer;

    public Init(GameService gameService, MessageProducer messageProducer) {

        this.gameService = gameService;
        this.messageProducer = messageProducer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void getSchedules() {
        Map<LocalDate, List<String>> gameMap = gameService.getGames(7);

        gameMap.forEach((k, v) -> {
            if (!gameMap.get(k).isEmpty()) {
                //Add date to the front of the list
                gameMap.get(k).add(0, k.getDayOfWeek().name() + " " + k.getDayOfMonth());
                messageProducer.sendMessage(gameMap.get(k));
            }
        });
    }
}
