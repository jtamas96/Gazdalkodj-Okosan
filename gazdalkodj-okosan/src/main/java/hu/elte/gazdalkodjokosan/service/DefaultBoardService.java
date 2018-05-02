package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.common.transfer.*;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.service.model.GameModel;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNotFoundException;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;

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
    public BoardResponse<List<Field>> getNewGame(int playerNumber) {
        try {
            model.newGame(playerNumber);
            return new BoardResponse<>("", true, model.getTable());

        } catch (PlayerNumberException ex) {
            return new BoardResponse<>("Not a valid player number!", false, null);
        }
    }

    @Override
    public BoardResponse<Integer> switchToNextPlayer(int playerIndex) {
        if (model.getCurrentPlayer().getIndex() == playerIndex) {
            model.switchPlayer();
            model.stepGame();

            int i = model.getCurrentPlayer().getIndex();
            return new BoardResponse<>("", true, i);

        } else {
            return new BoardResponse<>("Not your turn bro!", false, -1);
        }
    }

    @Override
    public BoardResponse<Player> getPlayer(int playerIndex) {
        Player cp = model.getCurrentPlayer();
        if (cp.getIndex() == playerIndex) {

            return new BoardResponse<>("", true, cp);

        } else {
            return new BoardResponse<>("You are not active!", false, null);
        }
    }

    private boolean itemPurchased(List<SaleItem> list, String itemName) {
        return list.stream().filter(i -> i.name.equals(itemName))
                .findFirst().orElseGet(() -> new SaleItem(Item.valueOf(itemName)))
                .isPurchased();
    }

    @Override
    public BoardResponse<String> buyCar(int playerIndex, boolean loan) {
        try {
            Player player = model.getCurrentPlayer();

            List<SaleItem> items = model.getItemsOfUser(playerIndex);
            boolean hasCar = itemPurchased(items, "AUTO");
            if (hasCar) {
                return new BoardResponse<>("You want more than one?", false, "");
            }
            if (player.getIndex() != playerIndex) {
                return new BoardResponse<>("Not your turn bro!", false, "");
            }

            int carCost = Item.AUTO.getCost();
            int bankBalance = player.getBankBalance();
            if (bankBalance >= carCost) {
                player.setBankBalance(bankBalance - carCost);
                return new BoardResponse<>("", true, "SUCCESS");
            } else {
                return new BoardResponse<>("Not enough money at your packet!", false, "");
            }

        } catch (PlayerNotFoundException e) {
            return new BoardResponse<>("Not Found player: " + playerIndex, false, "");
        }
    }

    @Override
    public BoardResponse buyHouse(int playerIndex, boolean loan) {
        return null;
    }

    @Override
    public BoardResponse buyInsurance(int playerIndex, Insurance insurance) {
        return null;
    }

    @Override
    public BoardResponse buyHouseAsset(int playerIndex, HouseAsset houseAsset) {
        return null;
    }

    @Override
    public BoardResponse buyBKVPass(int playerIndex) {
        return null;
    }

    @Override
    public List<Field> getTable() {
        return model.getTable();
    }

    @Override
    public BoardResponse<List<Player>> getPlayers() {
        return new BoardResponse<>("", true, model.getPlayers());
    }

    @Override
    public BoardResponse<Player> getCurrentPlayer() {
        return new BoardResponse<>("", true, model.getCurrentPlayer());
    }

    @Override
    public void stepGame() {
        model.stepGame();
    }
}
