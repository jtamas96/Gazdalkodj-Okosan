package hu.elte.go.dtos;

public class NewGameRequestDTO {
    int playerNumber;

    public NewGameRequestDTO(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
    public NewGameRequestDTO(){}
}
