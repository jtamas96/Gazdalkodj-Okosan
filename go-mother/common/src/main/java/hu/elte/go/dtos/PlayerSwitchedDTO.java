package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class PlayerSwitchedDTO {
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
}
