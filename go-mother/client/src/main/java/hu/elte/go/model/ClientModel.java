package hu.elte.go.model;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.BoardResponse;
import hu.elte.go.events.BuyEvent;
import hu.elte.go.events.GameOverEvent;
import hu.elte.go.events.GameSteppedEvent;
import hu.elte.go.events.MessageEvent;
import hu.elte.go.events.NewGameStartedEvent;
import hu.elte.go.events.UpdatePlayerEvent;
import hu.elte.go.persistence.IPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientModel {

    private List<Field> table;
    private List<Player> players;
    private Player currentPlayer;
    private final ApplicationEventPublisher publisher;
    private boolean gameOver;
    private IPersistence persistence;

    @Autowired
    public ClientModel(IPersistence persistence, ApplicationEventPublisher publisher) {
        this.persistence = persistence;
        this.publisher = publisher;
        this.gameOver = false;
    }

    public void newGame(int playerNumber) {
        persistence.requestNewGame(playerNumber);
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

    public void switchPlayer() {
        currentPlayer = persistence.switchPlayer(currentPlayer.getIndex());
    }

    public BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase) {
        return persistence.buyItems(itemsToPurchase);
    }

    @EventListener
    public void newGameStarted(NewGameStartedEvent event) {
        players = event.getPlayers();
        currentPlayer = event.getCurrentPlayer();
        publisher.publishEvent(new NewGameStartedEvent(this, event));
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
            table.get(currentPlayer.getPosition()).removePlayer(currentPlayer);
            Player player = event.getPlayer();
            players.set(player.getIndex(), player);
            publisher.publishEvent(new UpdatePlayerEvent(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyEvent event) {
        if (event.getSource().equals(persistence)) {
            publisher.publishEvent(new BuyEvent(this, event.getPlayer(), event.getItemPrices()));
        }
    }

    @EventListener
    public void GameOver(GameOverEvent event) {
        if (event.getSource().equals(persistence)) {
            publisher.publishEvent(new GameOverEvent(this, event.getWinners()));
            gameOver = true;
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
