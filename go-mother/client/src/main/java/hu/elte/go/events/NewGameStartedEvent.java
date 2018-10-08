package hu.elte.go.events;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class NewGameStartedEvent extends ApplicationEvent {

    private List<Field> table;
    private List<Player> players;
    
    public NewGameStartedEvent(Object source, List<Field> table, List<Player> players) {
        super(source);
        this.table = table;
        this.players = players;
    }

}
