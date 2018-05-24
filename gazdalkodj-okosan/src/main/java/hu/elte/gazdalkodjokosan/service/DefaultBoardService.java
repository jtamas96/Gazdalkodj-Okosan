package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.SaleItem;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.BuyEvent;
import hu.elte.gazdalkodjokosan.events.GameOverEvent;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.service.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultBoardService implements BoardService {

    private GameModel model;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public DefaultBoardService(GameModel model, ApplicationEventPublisher publisher) {
        this.model = model;
        this.publisher = publisher;
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
    public BoardResponse<Player> switchToNextPlayer(int playerIndex) {
        if (model.getCurrentPlayer().getIndex() == playerIndex) {
            model.switchPlayer();
            return new BoardResponse<>("", true, model.getCurrentPlayer());

        } else {
            return new BoardResponse<>("Not your turn bro!", false, null);
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

    @Override
    public BoardResponse<List<Item>> buyItems(List<Item> itemList) {
        Player currentPlayer = model.getCurrentPlayer();

        List<Item> purchasedNow = new ArrayList<>();
        List<Item> notEnoughMoneyFor = new ArrayList<>();
        for (Item item : itemList) {

            SaleItem currentItem = currentPlayer.getItem(item).get(); //TODO: What if not found?
            if (! currentItem.isPurchased()) {
//                return new BoardResponse<>("You want more than one from: " + item.name() + " ?", false, null);

                int bankBalance = currentPlayer.getBankBalance();
                if (bankBalance >= item.getCost()) {
                    currentPlayer.setBankBalance(bankBalance - item.getCost());

                    currentItem.purchase();
                    purchasedNow.add(item);
                } else {
                    notEnoughMoneyFor.add(item);
                }
            }
        }
        String notEnoughMoneyForString = "You dont have money for these: " + String.join(
                ",",
                notEnoughMoneyFor.stream().map(Enum::toString).collect(Collectors.toList()));
        if (purchasedNow.size() > 0) {
            return new BoardResponse<>(notEnoughMoneyForString, true, purchasedNow);
        } else {
            return new BoardResponse<>(notEnoughMoneyForString, false, purchasedNow);
        }
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

    @EventListener
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(model)) {
            publisher.publishEvent(new GameSteppedEvent(this, event.getCurrentPlayer(), event.getTable()));
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(model)) {
            publisher.publishEvent(new MessageEvent(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerEvent event) {
        if (event.getSource().equals(model)) {
            publisher.publishEvent(new UpdatePlayerEvent(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyEvent event) {
        if (event.getSource().equals(model)) {
            publisher.publishEvent(new BuyEvent(this, event.getPlayer(), event.getPurchaseAble()));
        }
    }
    
    @EventListener
    public void GameOver(GameOverEvent event) {
        if (event.getSource().equals(model)) {
            publisher.publishEvent(new GameOverEvent(this, event.getWinners()));
        }
    }
}
