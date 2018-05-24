package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Player;
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
