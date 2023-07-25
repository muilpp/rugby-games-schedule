package com.rugby.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Game implements Comparable<Game> {
    private String homeTeam;
    private String awayTeam;
    @Getter
    private LocalTime time;
    @Getter
    private String tournament;

    @Override
    public String toString() {
        return homeTeam + " vs " + awayTeam + " at " + time + " in " + tournament;
    }

    @Override
    public int compareTo(Game otherGame) {
        if (this.getTime().compareTo(otherGame.getTime()) == 0) {
           if (this.getTournament().equalsIgnoreCase(otherGame.getTournament())) {
               if (this.homeTeam.equalsIgnoreCase(otherGame.homeTeam) || this.awayTeam.equalsIgnoreCase(otherGame.awayTeam))
                    return 0;
               else return this.homeTeam.compareTo(otherGame.homeTeam) + this.awayTeam.compareTo(otherGame.awayTeam);
            } else {
               return this.getTournament().compareTo(otherGame.getTournament());
           }
        } else {
            return this.getTime().compareTo(otherGame.getTime());
        }
    }
}