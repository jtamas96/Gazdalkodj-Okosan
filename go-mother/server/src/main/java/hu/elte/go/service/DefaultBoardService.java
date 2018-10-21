package hu.elte.go.service;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.SaleItem;
import hu.elte.go.data.enums.Item;
import hu.elte.go.exceptions.PlayerNumberException;
import hu.elte.go.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
                int currentPrice = currentItem.cost - currentItem.getReducedPriceWith();
                if (bankBalance >= currentPrice) {
                    currentPlayer.setBankBalance(bankBalance - currentPrice);

                    currentItem.purchase();
                    if(item.insurrance){
                        currentPlayer.getInsurances().add(item);
                    }
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

//    @EventListener
//    public void GameStepped(GameSteppedEvent event) {
//       if (event.getSource().equals(model)) {
//
//        }
//    }
//
//    @EventListener
//    public void SendMessage(MessageEvent event) {
//       if (event.getSource().equals(model)) {
//
//        }
//    }
//
//    @EventListener
//    public void UpdatePlayer(PlayerUpdateEvent event) {
//        if (event.getSource().equals(model)) {
//
//        }
//    }
//
//    @EventListener
//    public void BuyItems(BuyEvent event) {
//        if (event.getSource().equals(model)) {
//            publisher.publishEvent(new BuyDTO(event.getPlayer(), event.getItemPrices()));
//        }
//    }
//
//    @EventListener
//    public void GameOver(GameOverEvent event) {
//        if (event.getSource().equals(model)) {
//
//        }
//   }
}
