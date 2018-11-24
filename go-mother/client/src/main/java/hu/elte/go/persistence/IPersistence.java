package hu.elte.go.persistence;

import java.util.List;

public interface IPersistence {

    void connect(String IPAddress);

    void createPlayer(String name);

    void requestNewGame(int playerNumber);
    
    void requestStep();

    void switchPlayer(int currentPlayerIndex);

    void buyItems(List<String> itemsToPurchase);

    void getRoomList();

    void createRoom(String roomName);
}
