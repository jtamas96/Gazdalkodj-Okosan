package hu.elte.go.persistence;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;

import java.util.List;

public interface IPersistence {
    
    void requestNewGame(int playerNumber);
    
    void requestStep();

    void switchPlayer(int currentPlayerIndex);

    BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase);
}
