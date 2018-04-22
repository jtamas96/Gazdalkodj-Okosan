package hu.elte.gazdalkodjokosan.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import hu.elte.gazdalkodjokosan.common.transfer.PlayerColor;
import hu.elte.gazdalkodjokosan.model.enums.Item;
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
    private Map<Player, List<SaleItem>> itemsMap;

    @Autowired
    private ApplicationEventPublisher publisher;

    public void newGame(int playerNumber) throws PlayerNumberException {
        if (playerNumber >= 2 && playerNumber <= 6) {
            players = new ArrayList<>();
            itemsMap = new HashMap<>();

            for (int i = 0; i < playerNumber; i++) {
                Player p = new Player(3000000, 238000, 0, 0, i, PlayerColor.values()[i]);
                players.add(p);
                itemsMap.put(p, SaleItem.getInitialListForUser());
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

    public boolean isGameOverForPlayer(PlayerColor playerColor) throws PlayerNotFoundException {
        Player player = getPlayerByColor(playerColor);

        List<SaleItem> items = getItemsOfUser(playerColor);

        boolean hasAllMandatory = items.stream()
                .filter(userItem -> Item.valueOf(userItem.name).getMandatory())
                .allMatch(userItem -> userItem.purchased);
        int allCash = player.getCash() + player.getDebt();
        return hasAllMandatory && allCash >= 600000;
    }

    public List<SaleItem> getItemsOfUser(PlayerColor playerColor) throws PlayerNotFoundException {
        Player player = getPlayerByColor(playerColor);
        return itemsMap.getOrDefault(player, new ArrayList<>());
    }

    private Player getPlayerByColor(PlayerColor playerColor) throws PlayerNotFoundException {
        Optional<Player> optPlayer = players.stream()
                .filter(p -> p.getColor() == playerColor)
                .findFirst();

        if (optPlayer.isPresent()) {
            return optPlayer.get();
        } else {
            throw new PlayerNotFoundException("Player not found with color: " + playerColor);
        }
    }
}
