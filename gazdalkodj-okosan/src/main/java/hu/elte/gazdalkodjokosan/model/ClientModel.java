package hu.elte.gazdalkodjokosan.model;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.persistence.IPersistence;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientModel {

    IPersistence persistence;
    List<Field> table;
    List<Player> players;
    private Player currentPlayer;

    @Autowired
    public ClientModel(IPersistence persistence) {
        this.persistence = persistence;
    }

    public void newGame(int playerNumber) throws PlayerNumberException {   
        table = persistence.requestNewGame(playerNumber);
        players = persistence.getPlayers();
        currentPlayer = persistence.getCurrentPlayer();
    }
    
    public void stepGame() {
        persistence.requestStep();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void endRound() {
        persistence.endRound(currentPlayer.getIndex());
    }
}
