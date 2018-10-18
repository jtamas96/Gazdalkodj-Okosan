package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class PlayerDTO {

    private Player player;
    
    public PlayerDTO(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

}
