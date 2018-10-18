package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class GameOverDTO {

    private final Player[] winners;

    public GameOverDTO(Player[] winners) {
        this.winners = winners;
    }

    public Player[] getWinners() {
        return winners;
    }

}
