package hu.elte.go.dtos;

import hu.elte.go.data.Player;

public class PlayerDTO {
    public String playerName;

    public PlayerDTO(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
