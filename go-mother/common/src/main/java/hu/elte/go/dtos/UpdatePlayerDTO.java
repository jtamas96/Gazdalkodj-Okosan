package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class UpdatePlayerDTO {

    private Player player;
    
    public UpdatePlayerDTO(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

}
