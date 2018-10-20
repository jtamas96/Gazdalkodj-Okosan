package hu.elte.go.dtos;

import hu.elte.go.data.Player;
import hu.elte.go.events.UpdatePlayerEvent;

public class PlayerUpdateDTO implements EventConvertible<UpdatePlayerEvent> {

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
    public UpdatePlayerEvent toEvent(Object source) {
        return new UpdatePlayerEvent(source, player);
    }
}
