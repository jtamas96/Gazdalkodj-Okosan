package hu.elte.go.events;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class NewGameStartedEvent extends ApplicationEvent {

    private List<Field> table;
    private List<Player> players;
    private Player currentPlayer;

    public NewGameStartedEvent(Object source, List<Field> table, List<Player> players, Player currentPlayer) {
        super(source);
        this.table = table;
        this.players = players;
        this.currentPlayer = currentPlayer;
    }

    public List<Field> getTable() {
        return table;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

}
