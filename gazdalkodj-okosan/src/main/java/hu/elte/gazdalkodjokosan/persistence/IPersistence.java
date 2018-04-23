package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.common.transfer.PlayerStatus;
import hu.elte.gazdalkodjokosan.data.Field;

import java.util.List;

public interface IPersistence {

    public PlayerStatus getPlayer(int index);
    
    public List<Field> getFields();
    
    public void requestNewGame(int playerNumber);
}
