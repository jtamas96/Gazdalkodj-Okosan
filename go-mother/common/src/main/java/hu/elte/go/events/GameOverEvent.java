package hu.elte.go.events;

import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

public class GameOverEvent extends ApplicationEvent {

    private final Player[] winners;

    public GameOverEvent(Object source, Player[] winners) {
        super(source);
        this.winners = winners;
    }

    public Player[] getWinners() {
        return winners;
    }

}
