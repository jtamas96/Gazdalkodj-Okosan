package hu.elte.go.dtos;

import hu.elte.go.data.Player;
import hu.elte.go.events.PlayerUpdateEvent;

public class PlayerUpdateDTO implements EventConvertible<PlayerUpdateEvent> {

    private Player player;
    
    public PlayerUpdateDTO(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

    public PlayerUpdateDTO(){}

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public PlayerUpdateEvent toEvent(Object source) {
        return new PlayerUpdateEvent(source, player);
    }
}
