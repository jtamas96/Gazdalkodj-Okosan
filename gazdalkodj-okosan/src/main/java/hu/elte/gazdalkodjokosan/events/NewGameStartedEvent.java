package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import java.util.List;
import org.springframework.context.ApplicationEvent;

public class NewGameStartedEvent extends ApplicationEvent {

    private List<Field> table;
    private List<Player> players;
    
    public NewGameStartedEvent(Object source, List<Field> table, List<Player> players) {
        super(source);
        this.table = table;
        this.players = players;
    }

}
