package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.Field;
import java.util.List;

public interface IPersistence {

    public Player getPlayer(int index);
    
    public List<Field> updateFields();
    
    public void requestNewGame(int playerNumber);
}
