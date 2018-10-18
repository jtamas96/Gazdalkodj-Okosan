package hu.elte.go.events;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

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
