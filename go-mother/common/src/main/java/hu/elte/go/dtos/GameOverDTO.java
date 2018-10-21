package hu.elte.go.dtos;

import hu.elte.go.data.Player;
import hu.elte.go.events.GameOverEvent;

public class GameOverDTO implements EventConvertible<GameOverEvent> {

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

    @Override
    public GameOverEvent toEvent(Object source) {
        return new GameOverEvent(source, winners);
    }
}
