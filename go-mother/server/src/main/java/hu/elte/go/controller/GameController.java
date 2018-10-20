package hu.elte.go.controller;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.events.BuyEvent;
import hu.elte.go.events.GameOverEvent;
import hu.elte.go.events.GameSteppedEvent;
import hu.elte.go.events.MessageEvent;
import hu.elte.go.events.UpdatePlayerEvent;
import hu.elte.go.exceptions.BuyException;
import hu.elte.go.exceptions.PlayerNumberException;
import hu.elte.go.model.GameModel;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameModel gameModel;

    @Autowired
    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @MessageMapping("/step")
    public void stepGame() {
        gameModel.stepGame();
    }

    @MessageMapping("/newGame")
    @SendTo("/newGameResponse")
    public BoardResponse<NewGameStartedDTO> newGame(NewGameRequestDTO newGameRequestDTO) {
        System.out.println("new game ms received");
        try {
            gameModel.newGame(newGameRequestDTO.getPlayerNumber());
            List<Player> players = gameModel.getPlayers();
            NewGameStartedDTO response = new NewGameStartedDTO(gameModel.getTable(), players, gameModel.getCurrentPlayer());
            return new BoardResponse<>("", true, response);
        } catch (PlayerNumberException ex) {
            return new BoardResponse<>(ex.getMessage(), false, null);
        }
    }

    @MessageMapping("/switchPlayer")
    @SendTo("/switchPlayerResponse")
    public BoardResponse<PlayerSwitchedDTO> switchPlayer(int playerIndex) {
        if (gameModel.getCurrentPlayer().getIndex() == playerIndex) {
            gameModel.switchPlayer();
            PlayerSwitchedDTO playerDTO = new PlayerSwitchedDTO(gameModel.getCurrentPlayer());
            return new BoardResponse<>("", true, playerDTO);
        } else {
            return new BoardResponse<>("Not your turn bro!", false, null);
        }
    }

    @MessageMapping("/buyItems")
    @SendTo("/buyItemsResponse")
    public BoardResponse<List<Item>> buyItems(List<Item> itemList) {
        try {
            List<Item> bought = gameModel.buyItems(itemList);
            return new BoardResponse("", true, bought);
        } catch (BuyException ex) {
            return new BoardResponse(ex.getMessage(), false, null);
        }
    }

    @EventListener
    @SendTo("/gameStepped")
    public BoardResponse<GameSteppedDTO> gameStepped(GameSteppedEvent event) {
        GameSteppedDTO dto = new GameSteppedDTO(event.getCurrentPlayer(), event.getTable());
        return new BoardResponse<>("", true, dto);
    }

    @EventListener
    @SendTo("/messages")
    public MessageDTO sendMessage(MessageEvent event) {
        return new MessageDTO(event.getMessage());
    }

    @EventListener
    @SendTo("/playerUpdates")
    public PlayerUpdateDTO updatePlayer(UpdatePlayerEvent event) {
        return new PlayerUpdateDTO(event.getPlayer());
    }

    @EventListener
    @SendTo("/buyEvents")
    public BuyDTO buyEvent(BuyEvent event) {
        return new BuyDTO(event.getPlayer(), event.getItemPrices());
    }

    @EventListener
    @SendTo("/gameOver")
    public GameOverDTO gameOver(GameOverEvent event) {
        return new GameOverDTO(event.getWinners());
    }
}
