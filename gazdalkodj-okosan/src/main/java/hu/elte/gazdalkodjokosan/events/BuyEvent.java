package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Player;
import org.springframework.context.ApplicationEvent;

public class BuyEvent extends ApplicationEvent {
    Player player;

    public BuyEvent(Object source, Player player) {
        super(source);
        this.player = player;
    }

    public Player getPlayer() {
        return player;

    }
}
