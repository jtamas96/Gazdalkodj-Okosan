package hu.elte.gazdalkodjokosan.model;

import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.persistence.IPersistence;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientModel {

    private IPersistence persistence;
    private List<Field> table;
    private List<Player> players;
    private Player currentPlayer;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ClientModel(IPersistence persistence, ApplicationEventPublisher publisher) {
        this.persistence = persistence;
        this.publisher = publisher;
    }

    public void newGame(int playerNumber) throws PlayerNumberException {   
        table = persistence.requestNewGame(playerNumber);
        players = persistence.getPlayers();
        currentPlayer = persistence.getCurrentPlayer();
    }
    
    public void stepGame() {
        persistence.requestStep();
    }
    
    @EventListener
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(persistence)) {
            table = event.getTable();
            currentPlayer.setPosition(event.getCurrentPlayer().getPosition());
            publisher.publishEvent(new GameSteppedEvent(this, event.getCurrentPlayer(), event.getTable()));
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(persistence)) {
            publisher.publishEvent(new MessageEvent(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerEvent event) {
        if (event.getSource().equals(persistence)) {
            Player player = event.getPlayer();
            players.set(player.getIndex(), player);
            publisher.publishEvent(new UpdatePlayerEvent(this, event.getPlayer()));
        }
    }

}
