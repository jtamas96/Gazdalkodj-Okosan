package hu.elte.go.model;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.SaleItem;
import hu.elte.go.data.enums.Item;
import hu.elte.go.events.*;
import hu.elte.go.persistence.IPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ClientModel {

    private List<Field> table;
    private List<Player> players;
    private Player currentPlayer;
    private final ApplicationEventPublisher publisher;
    private boolean gameOver;
    private final IPersistence persistence;

    @Autowired
    public ClientModel(IPersistence persistence, ApplicationEventPublisher publisher) {
        this.persistence = persistence;
        this.publisher = publisher;
        this.gameOver = false;
    }

    public void newGame(int playerNumber) {
        // persistence.requestNewGame(playerNumber);
        persistence.createPlayer("Proba");
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
        persistence.switchPlayer(currentPlayer.getIndex());
    }

    public void buyItems(List<String> itemsToPurchase) {
        persistence.buyItems(itemsToPurchase);
    }

    @EventListener
    public void playerCreated(PlayerCreatedEvent event) {
        if(event.getSource().equals(persistence)){
            System.out.println("Player created");
        }
    }

    @EventListener
    public void newGameStarted(NewGameStartedEvent event) {
        if(event.getSource().equals(persistence)){
            players = event.getPlayers();
            currentPlayer = event.getCurrentPlayer();
            publisher.publishEvent(new NewGameStartedEvent(this, event));
        }
    }
    
    @EventListener
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(persistence)) {
            table = event.getTable();
            currentPlayer.setPosition(event.getCurrentPlayer().getPosition());
            publisher.publishEvent(new GameSteppedEvent(this, event));
        }
    }

    @EventListener
    public void playerSwitched(PlayerSwitchedEvent event){
        if (event.getSource().equals(persistence)) {
            currentPlayer = event.getPlayer();
            publisher.publishEvent(new PlayerSwitchedEvent(this, currentPlayer));
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(persistence)) {
            publisher.publishEvent(new MessageEvent(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(PlayerUpdateEvent event) {
        if (event.getSource().equals(persistence)) {
            table.get(currentPlayer.getPosition()).removePlayer(currentPlayer);
            Player player = event.getPlayer();
            players.set(player.getIndex(), player);
            publisher.publishEvent(new PlayerUpdateEvent(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyEvent event) {
        if (event.getSource().equals(persistence)) {
            publisher.publishEvent(new BuyEvent(this, event.getPlayer(), event.getItemPrices()));
        }
    }

    @EventListener
    public void ItemsPurchased(ItemsPurchasedEvent event) {
        if (event.getSource().equals(persistence)) {
            Map<String, Integer> bought = event.getItems();
            bought.keySet().forEach(
                    name -> {
                        Optional<SaleItem> siOpt = currentPlayer.getItem(Item.valueOf(name));
                        siOpt.ifPresent(SaleItem::purchase);
                    });
            publisher.publishEvent(new ItemsPurchasedEvent(this, event.getItems()));
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
