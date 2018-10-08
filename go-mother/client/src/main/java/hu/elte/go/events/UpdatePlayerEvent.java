package hu.elte.go.events;

import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

public class UpdatePlayerEvent extends ApplicationEvent {

    private Player player;
    
    public UpdatePlayerEvent(Object source, Player player) {
        super(source);
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

}
