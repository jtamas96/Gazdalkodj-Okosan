package hu.elte.go.events;

import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

public class PlayerSwitchedEvent extends ApplicationEvent {
    private Player player;

    public PlayerSwitchedEvent(Object source, Player player) {
        super(source);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
