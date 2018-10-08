package hu.elte.go.model;

import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.BoardResponse;
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

    @Autowired
    public ClientModel(IPersistence persistence, ApplicationEventPublisher publisher) {
        this.persistence = persistence;
        this.publisher = publisher;
        this.gameOver = false;
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

    public void switchPlayer() {
        currentPlayer = persistence.switchPlayer(currentPlayer.getIndex());
    }

    public BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase) {
        return persistence.buyItems(itemsToPurchase);
    }

//    @EventListener
//    public void GameStepped(GameSteppedDTO event) {
//        if (event.getSource().equals(persistence)) {
//            table = event.getTable();
//            currentPlayer.setPosition(event.getCurrentPlayer().getPosition());
//            publisher.publishEvent(new GameSteppedDTO(this, event.getCurrentPlayer(), event.getTable()));
//        }
//    }
//
//    @EventListener
//    public void SendMessage(MessageDTO event) {
//        if (event.getSource().equals(persistence)) {
//            publisher.publishEvent(new MessageDTO(this, event.getMessage()));
//        }
//    }
//
//    @EventListener
//    public void UpdatePlayer(UpdatePlayerDTO event) {
//        if (event.getSource().equals(persistence)) {
////            table.get(currentPlayer.getPosition()).removePlayer(currentPlayer);
////            Player player = event.getPlayer();
////            players.set(player.getIndex(), player);
//            publisher.publishEvent(new UpdatePlayerDTO(this, event.getPlayer()));
//        }
//    }
//
//    @EventListener
//    public void BuyItems(BuyDTO event) {
//        if (event.getSource().equals(persistence)) {
//            publisher.publishEvent(new BuyDTO(this, event.getPlayer(), event.getItemPrices()));
//        }
//    }
//
//    @EventListener
//    public void GameOver(GameOverDTO event) {
//        if (event.getSource().equals(persistence)) {
//            publisher.publishEvent(new GameOverDTO(this, event.getWinners()));
//            gameOver = true;
//        }
//    }

    public boolean isGameOver() {
        return gameOver;
    }
}
