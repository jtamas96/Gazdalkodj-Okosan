package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;

import java.util.List;

public interface IPersistence {

    public Player getPlayer(int index);
    
    public List<Field> getFields();
    
    public List<Field> requestNewGame (int playerNumber) throws PlayerNumberException;
    
    public List<Player> getPlayers();
    
    public Player getCurrentPlayer();
    
    public void requestStep();

    Player switchPlayer(int currentPlayerIndex);

    BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase);
}
