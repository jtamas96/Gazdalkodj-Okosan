package hu.elte.go.persistence;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.events.*;
import hu.elte.go.exceptions.*;
import hu.elte.go.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Persistence implements IPersistence {

    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public Persistence(BoardService boardService, ApplicationEventPublisher publisher) {
        this.boardService = boardService;
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
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameSteppedEvent(this, event.getCurrentPlayer(), event.getTable()));
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new MessageEvent(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerEvent event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new UpdatePlayerEvent(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyEvent event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new BuyEvent(this, event.getPlayer(), event.getItemPrices()));
        }
    }

    @EventListener
    public void GameOver(GameOverEvent event) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameOverEvent(this, event.getWinners()));
        }
    }
}
