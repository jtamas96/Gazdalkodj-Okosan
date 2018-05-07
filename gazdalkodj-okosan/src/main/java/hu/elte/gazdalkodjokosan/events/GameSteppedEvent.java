package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import java.util.List;
import org.springframework.context.ApplicationEvent;

public class GameSteppedEvent extends ApplicationEvent {

    private final Player currentPlayer;
    private final List<Field> table;
    
    public GameSteppedEvent(Object source, Player currentPlayer, List<Field> table) {
        super(source);
        this.currentPlayer = currentPlayer;
        this.table = table;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public List<Field> getTable() {
        return table;
    }
}
