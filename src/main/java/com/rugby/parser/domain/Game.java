package com.rugby.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@Setter
public class Game {
    private String homeTeam;
    private String awayTeam;
    @Getter
    private LocalTime time;
    private String tournament;

    @Override
    public String toString() {
        return homeTeam + " vs " + awayTeam + " at " + time + " in " + tournament;
    }
}
