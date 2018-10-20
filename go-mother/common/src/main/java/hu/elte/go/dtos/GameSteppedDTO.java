package hu.elte.go.dtos;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.events.GameSteppedEvent;

import java.util.List;

public class GameSteppedDTO implements EventConvertible<GameSteppedEvent> {

    private Player currentPlayer;
    private List<Field> table;
    
    public GameSteppedDTO(Player currentPlayer, List<Field> table) {
        this.currentPlayer = currentPlayer;
        this.table = table;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public List<Field> getTable() {
        return table;
    }

    public GameSteppedDTO() {
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setTable(List<Field> table) {
        this.table = table;
    }

    @Override
    public GameSteppedEvent toEvent(Object source) {
        return new GameSteppedEvent(source, currentPlayer, table);
    }
}
