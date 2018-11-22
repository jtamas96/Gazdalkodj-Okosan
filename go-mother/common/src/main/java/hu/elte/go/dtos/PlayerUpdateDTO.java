package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class PlayerUpdateDTO {

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
}
