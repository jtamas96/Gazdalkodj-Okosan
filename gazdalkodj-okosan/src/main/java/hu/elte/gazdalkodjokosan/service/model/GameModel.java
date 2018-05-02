package hu.elte.gazdalkodjokosan.service.model;


import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNotFoundException;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameModel {

    private List<Player> players;
    private List<Field> table;
    private Player currentPlayer;
    private final ApplicationEventPublisher publisher;
    
    @Autowired
    public GameModel(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void newGame(int playerNumber) throws PlayerNumberException {
        if (playerNumber >= 2 && playerNumber <= 6) {
            players = new ArrayList<>();

            for (int i = 0; i < playerNumber; i++) {
                Player p = new Player(3000000, 0, 0, i, SaleItem.getInitialListForUser());
                players.add(p);
            }
            currentPlayer = players.get(0);
            table = new ArrayList<>();
            table.add(new Field(0, players));
            for (int i = 1; i < 42; i++) {
                table.add(new Field(i, new ArrayList<>()));
            }
        } else {
            throw new PlayerNumberException("Invalid number of players.");
        }
    }

    public void stepGame() {
        Random random = new Random();
        int step = random.nextInt(6) + 1;
        int currentPosition = currentPlayer.getPosition();
        table.get(currentPosition).removePlayer(currentPlayer);
        if (currentPosition + step < table.size()) {
            table.get(currentPosition + step).addPlayer(currentPlayer);
            currentPlayer.setPosition(currentPosition + step);
        } else {
            int nextPosition = step + currentPosition - table.size() - 1;
            if (nextPosition == 0) {
                playerSteppedOnStart(true);
            }else{
                playerSteppedOnStart(false);
            }
            table.get(nextPosition).addPlayer(currentPlayer);
            currentPlayer.setPosition(nextPosition);
        }
        publisher.publishEvent(new GameSteppedEvent(this));
    }

    private void playerSteppedOnStart(boolean exact) {
        int currentBalance = currentPlayer.getBankBalance();
        if (exact) {
            currentPlayer.setBankBalance(currentBalance + 1000000);
        } else {
            currentPlayer.setBankBalance(currentBalance + 500000);
        }
    }

    public List<Field> getTable() {
        return table;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOverForPlayer(int playerIndex) throws PlayerNotFoundException {
        Player player = getPlayer(playerIndex);

        List<SaleItem> items = getItemsOfUser(playerIndex);

        boolean hasAllMandatory = items.stream()
                .filter(userItem -> Item.valueOf(userItem.name).getMandatory())
                .allMatch(SaleItem::isPurchased);
        return hasAllMandatory && player.getBankBalance() >= 600000;
    }

    public List<SaleItem> getItemsOfUser(int playerIndex) throws PlayerNotFoundException {
        Player player = getPlayer(playerIndex);
        return player.getItems();
    }

    private Player getPlayer(int playerIndex) throws PlayerNotFoundException {
        Optional<Player> optPlayer = players.stream()
                .filter(p -> p.getIndex() == playerIndex)
                .findFirst();

        if (optPlayer.isPresent()) {
            return optPlayer.get();
        } else {
            throw new PlayerNotFoundException("Player not found with color: " + playerIndex);
        }
    }

    public void switchPlayer(){
        int newPlayersIndex = (currentPlayer.getIndex() + 1) % players.size();
        currentPlayer = players.get(newPlayersIndex);
    }
    
    public List<Player> getPlayers() {
        return players;
    }
}
