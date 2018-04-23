package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.common.transfer.*;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.service.model.GameModel;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNotFoundException;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;

import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultBoardService implements BoardService {
    private GameModel model;

    @Autowired
    public DefaultBoardService(GameModel model) {
        this.model = model;
    }

    @Override
    public BoardResponse<Integer> getNewGame(int playerNumber) {
        try {
            model.newGame(playerNumber);
            int i = model.getCurrentPlayer().getIndex();
            return new BoardResponse<>("", true, i);

        } catch (PlayerNumberException ex) {
            return new BoardResponse<>("Not a valid player number!", false, -1);
        }
    }

    @Override
    public BoardResponse<Integer> switchToNextPlayer(int playerIndex) {
        if(model.getCurrentPlayer().getIndex() == playerIndex){
            model.switchPlayer();
            model.stepGame();

            int i = model.getCurrentPlayer().getIndex();
            return new BoardResponse<>("", true, i);

        }else{
            return new BoardResponse<>("Not your turn bro!", false, -1);
        }
    }

    @Override
    public BoardResponse<PlayerStatus> getPlayerStatus(int playerIndex) {
        Player cp = model.getCurrentPlayer();
        if(cp.getIndex() == playerIndex){
            try {
                boolean isGameOver = model.isGameOverForPlayer(playerIndex);
                int cash = cp.getCash();
                int debt = cp.getDebt();
                int index = cp.getIndex();

                List<SaleItem> items = model.getItemsOfUser(playerIndex);

                boolean hasHouse = itemPurchased(items, "LAKAS");
                boolean hasCar = itemPurchased(items, "AUTO");
                boolean hasBKV = itemPurchased(items, "BKV_BERLET");

                //TODO: fill sets, Andor.
                PlayerStatus result = new PlayerStatus(isGameOver, cash, debt, index, hasHouse, hasCar, hasBKV, new HashSet<>(), new HashSet<>());
                return new BoardResponse<>("", true, result);

            } catch (PlayerNotFoundException e) {
                return new BoardResponse<>("Not found player wiht index: " + playerIndex, false, null);
            }

        }else{
            return new BoardResponse<>("You are not active!", false, null);
        }
    }

    private boolean itemPurchased(List<SaleItem> list, String itemName){
        return list.stream().filter(i -> i.name.equals(itemName))
                .findFirst().orElseGet(() -> new SaleItem(Item.valueOf(itemName)))
                .isPurchased();
    }

    @Override
    public BoardResponse<String> buyCar(int playerIndex, boolean loan, boolean payWithCash) {
        try {
            Player player = model.getCurrentPlayer();

            List<SaleItem> items = model.getItemsOfUser(playerIndex);
            boolean hasCar = itemPurchased(items, "AUTO");
            if(hasCar){
                return new BoardResponse<>("You want more than one?", false, "");
            }
            if(player.getIndex() != playerIndex){
                return new BoardResponse<>("Not your turn bro!", false, "");
            }

            int carCost = Item.AUTO.getCost();
            if(payWithCash){
                int cash = player.getCash();

                if (cash >= carCost) { //TODO: buy from DEBT? andor
                    player.setCash(cash - carCost);
                    return new BoardResponse<>("", true, "SUCCESS");
                } else return new BoardResponse<>("Not enough money at your packet!", false, "");
            } else {
                int bankBalance = player.getBankBalance();
                if (bankBalance >= carCost) {
                    player.setBankBalance(bankBalance - carCost);
                    return new BoardResponse<>("", true, "SUCCESS");
                } else return new BoardResponse<>("Not enough money at your packet!", false, "");
            }

        } catch (PlayerNotFoundException e) {
            return new BoardResponse<>("Not Found player: " + playerIndex, false, "");
        }
    }

    @Override
    public BoardResponse buyHouse(int playerIndex, boolean loan, boolean payWithCash) {
        return null;
    }

    @Override
    public BoardResponse buyInsurance(int playerIndex, boolean payWithCash, Insurance insurance) {
        return null;
    }

    @Override
    public BoardResponse buyHouseAsset(int playerIndex, boolean payWithCash, HouseAsset houseAsset) {
        return null;
    }

    @Override
    public BoardResponse buyBKVPass(int playerIndex, boolean payWithCash) {
        return null;
    }

    @Override
    public BoardResponse<String> transferMoney(int playerIndex, int amount) {
        Player cp = model.getCurrentPlayer();
        if(cp.getIndex() == playerIndex){
            int cash = cp.getCash();

            if(cash >= amount){
                int debt = cp.getDebt();
                cp.setBankBalance(debt + amount);
                cp.setCash(cash - amount);

                return new BoardResponse<>("", true, "");
            }else return new BoardResponse<>("Not enough money!", false, "");
        }else{
            return new BoardResponse<>("Not Authorized for this!", false, "");
        }
    }

    @Override
    public List<Field> getTable() {
        return model.getTable();
    }
}
