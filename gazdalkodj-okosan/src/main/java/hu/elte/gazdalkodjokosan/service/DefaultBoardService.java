package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.common.transfer.*;
import hu.elte.gazdalkodjokosan.model.GameModel;
import hu.elte.gazdalkodjokosan.model.Player;
import hu.elte.gazdalkodjokosan.model.SaleItem;
import hu.elte.gazdalkodjokosan.model.enums.Item;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNotFoundException;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;

import java.util.HashSet;
import java.util.List;

public class DefaultBoardService implements BoardService {
    private GameModel model;

    public DefaultBoardService() {
        this.model = new GameModel();
    }

    @Override
    public PlayerColor getNewGame(int playerNumber) {
        try {
            model.newGame(playerNumber);
            return model.getCurrentPlayer().getColor();
        } catch (PlayerNumberException ex) {
            //TODO: error message to user
            return null;
        }
    }

    @Override
    public PlayerColor switchToNextPlayer(PlayerColor playerColor) {
        if(model.getCurrentPlayer().getColor() == playerColor){
            model.stepGame();
            return model.getCurrentPlayer().getColor();

        }else{
            //TODO: Error message to user. NOT current player trying to step.
            return null;
        }
    }

    @Override
    public PlayerStatus getPlayerStatus(PlayerColor playerColor) {
        Player cp = model.getCurrentPlayer();
        if(cp.getColor() == playerColor){
            try {
                boolean isGameOver = model.isGameOverForPlayer(playerColor);
                int cash = cp.getCash();
                int debt = cp.getDebt();
                int index = cp.getIndex();

                List<SaleItem> items = model.getItemsOfUser(playerColor);

                boolean hasHouse = itemPurchased(items, "LAKAS");
                boolean hasCar = itemPurchased(items, "AUTO");
                boolean hasBKV = itemPurchased(items, "BKV_BERLET");

                //TODO: fill sets, Andor.
                return new PlayerStatus(isGameOver, cash, debt, index, hasHouse, hasCar, hasBKV, new HashSet<>(), new HashSet<>());

            } catch (PlayerNotFoundException e) {
                // TODO: Error message to client.
                return null;
            }

        }else{
            //TODO: Status of the not current player. Rethink the logic.
            return null;
        }
    }

    private boolean itemPurchased(List<SaleItem> list, String itemName){
        return list.stream().filter(i -> i.name.equals(itemName))
                .findFirst().orElseGet(() -> new SaleItem(Item.valueOf(itemName)))
                .isPurchased();
    }

    @Override
    public BoardResponse buyCar(PlayerColor playerColor, boolean loan, boolean payWithCash) {
        try {
            Player player = model.getCurrentPlayer();

            List<SaleItem> items = model.getItemsOfUser(playerColor);
            boolean hasCar = itemPurchased(items, "AUTO");
            if(hasCar){
                return new BoardResponse("You want more than one?", false);
            }
            if(! player.getColor().equals(playerColor)){
                return new BoardResponse("Not your turn bro!", false);
            }

            int carCost = Item.AUTO.getCost();
            if(payWithCash){
                int cash = player.getCash();

                if (cash >= carCost) { //TODO: buy from DEBT? andor
                    player.setCash(cash - carCost);
                    return new BoardResponse("", true);
                } else return new BoardResponse("Not enough money at your packet!", false);
            } else {
                int bankBalance = player.getBankBalance();
                if (bankBalance >= carCost) {
                    player.setBankBalance(bankBalance - carCost);
                    return new BoardResponse("", true);
                } else return new BoardResponse("Not enough money at your packet!", false);
            }

        } catch (PlayerNotFoundException e) {
            return new BoardResponse("Not Found player with this color: " + playerColor, false);
        }
    }

    @Override
    public BoardResponse buyHouse(PlayerColor playerColor, boolean loan, boolean payWithCash) {
        return null;
    }

    @Override
    public BoardResponse buyInsurance(PlayerColor playerColor, boolean payWithCash, Insurance insurance) {
        return null;
    }

    @Override
    public BoardResponse buyHouseAsset(PlayerColor playerColor, boolean payWithCash, HouseAsset houseAsset) {
        return null;
    }

    @Override
    public BoardResponse buyBKVPass(PlayerColor playerColor, boolean payWithCash) {
        return null;
    }

    @Override
    public BoardResponse transferMoney(PlayerColor playerColor, int amount) {
        Player cp = model.getCurrentPlayer();
        if(cp.getColor() == playerColor){
            int cash = cp.getCash();

            if(cash >= amount){
                int debt = cp.getDebt();
                cp.setBankBalance(debt + amount);
                cp.setCash(cash - amount);

                return new BoardResponse("", true);
            }else return new BoardResponse("Not enough money!", false);
        }else{
            return new BoardResponse("Not Authorized for this!", false);
        }
    }
}
