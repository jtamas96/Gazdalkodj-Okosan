package hu.elte.go.dtos;

import hu.elte.go.data.Player;
import hu.elte.go.events.PlayerSwitchedEvent;

public class PlayerSwitchedDTO  implements EventConvertible<PlayerSwitchedEvent> {
    private Player player;

    public PlayerSwitchedDTO(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerSwitchedDTO(){}

    public void setPlayer(Player player) {
        this.player = player;
    }
    @Override
    public PlayerSwitchedEvent toEvent(Object source) {
        return new PlayerSwitchedEvent(source, player);
    }
}
