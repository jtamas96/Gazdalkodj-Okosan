package hu.elte.go.dtos;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;

import java.util.List;

public class GameSteppedDTO {

    private final Player currentPlayer;
    private final List<Field> table;
    
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
}
