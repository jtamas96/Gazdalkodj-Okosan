package hu.elte.go.dtos;

public class NewGameRequest {
    int playerNumber;

    public NewGameRequest(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
