package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class GameOverDTO  {

    private Player[] winners;

    public GameOverDTO(Player[] winners) {
        this.winners = winners;
    }

    public Player[] getWinners() {
        return winners;
    }

    public GameOverDTO(){}

    public void setWinners(Player[] winners) {
        this.winners = winners;
    }
}
