package hu.elte.go.dtos;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;

import java.util.List;

public class NewGameStartedDTO {

    private List<Field> table;
    private List<Player> players;
    
    public NewGameStartedDTO(List<Field> table, List<Player> players) {
        this.table = table;
        this.players = players;
    }

}
