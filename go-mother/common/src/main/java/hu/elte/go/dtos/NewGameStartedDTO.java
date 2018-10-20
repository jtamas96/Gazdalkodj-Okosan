package hu.elte.go.dtos;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.events.NewGameStartedEvent;

import java.util.List;

public class NewGameStartedDTO implements EventConvertible<NewGameStartedEvent> {

    private List<Field> table;
    private List<Player> players;
    private Player currentPlayer;
    
    public NewGameStartedDTO(List<Field> table, List<Player> players, Player currentPlayer) {
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
    public NewGameStartedDTO(){}

    public void setTable(List<Field> table) {
        this.table = table;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public NewGameStartedEvent toEvent(Object source) {
        return new NewGameStartedEvent(source, table, players, currentPlayer);
    }
}
