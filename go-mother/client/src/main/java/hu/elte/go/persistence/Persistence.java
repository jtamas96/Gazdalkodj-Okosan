package hu.elte.go.persistence;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;

import java.util.List;

@Component
public class Persistence implements IPersistence {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public Persistence(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public Player getPlayer(int index) {
        return boardService.getPlayer(index).getValue();
    }

    @Override
    public List<Field> getFields() {
        return boardService.getTable();
    }

    @Override
    public List<Field> requestNewGame(int playerNumber) throws PlayerNumberException {
        BoardResponse<List<Field>> response = boardService.getNewGame(playerNumber);

        if (!response.isActionSuccessful()) {
            throw new PlayerNumberException(response.getErrorMessage());
        }

        return response.getValue();
    }

    @Override
    public List<Player> getPlayers() {
        return boardService.getPlayers().getValue();
    }

    @Override
    public Player getCurrentPlayer() {
        return boardService.getCurrentPlayer().getValue();
    }

    @Override
    public void requestStep() {
        boardService.stepGame();
    }

    @Override
    public Player switchPlayer(int currentPlayerIndex) {
        BoardResponse<Player> resp = boardService.switchToNextPlayer(currentPlayerIndex);
        if (resp.isActionSuccessful()) {
            return resp.getValue();
        } else {
            System.out.println("Error:" + resp.getErrorMessage());
        }
        return null;
    }

    @Override
    public BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase) {
        return boardService.buyItems(itemsToPurchase);
    }

    @EventListener
    public void GameStepped(GameSteppedDTO event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameSteppedDTO(this, event.getCurrentPlayer(), event.getTable()));
        }
    }

    @EventListener
    public void SendMessage(MessageDTO event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new MessageDTO(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerDTO event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new UpdatePlayerDTO(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyDTO event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new BuyDTO(this, event.getPlayer(), event.getItemPrices()));
        }
    }

    @EventListener
    public void GameOver(GameOverDTO event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameOverDTO(this, event.getWinners()));
        }
    }
}
