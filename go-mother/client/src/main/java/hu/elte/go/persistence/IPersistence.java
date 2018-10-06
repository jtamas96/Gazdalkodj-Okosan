package hu.elte.go.persistence;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.exceptions.*;

import java.util.List;

public interface IPersistence {

    public Player getPlayer(int index);
    
    public List<Field> getFields();
    
    public List<Field> requestNewGame(int playerNumber) throws PlayerNumberException;
    
    public List<Player> getPlayers();
    
    public Player getCurrentPlayer();
    
    public void requestStep();

    Player switchPlayer(int currentPlayerIndex);

    BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase);
}
