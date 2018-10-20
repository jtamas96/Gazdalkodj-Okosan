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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameModel gameModel;
    private SimpMessagingTemplate template;

    @Autowired
    public GameController(GameModel gameModel, SimpMessagingTemplate template) {
        this.gameModel = gameModel;
        this.template = template;
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
            return new BoardResponse<>("", true, bought);
        } catch (BuyException ex) {
            return new BoardResponse<>(ex.getMessage(), false, null);
        }
    }

    @EventListener
    public void gameStepped(GameSteppedEvent event) {
        GameSteppedDTO dto = new GameSteppedDTO(event.getCurrentPlayer(), event.getTable());
        BoardResponse<GameSteppedDTO> response = new BoardResponse<>("", true, dto);
        this.template.convertAndSend("/gameStepped", response);
    }

    @EventListener
    public void sendMessage(MessageEvent event) {
        MessageDTO dto = new MessageDTO(event.getMessage());
        BoardResponse<MessageDTO> response = new BoardResponse<>("", true, dto);
        this.template.convertAndSend("/messages", response);
    }

    @EventListener
    public void updatePlayer(UpdatePlayerEvent event) {
        PlayerUpdateDTO dto = new PlayerUpdateDTO(event.getPlayer());
        BoardResponse<PlayerUpdateDTO> response = new BoardResponse<>("", true, dto);
        this.template.convertAndSend("/playerUpdates", response);
    }

    @EventListener
    public void buyEvent(BuyEvent event) {
        BuyDTO dto = new BuyDTO(event.getPlayer(), event.getItemPrices());
        BoardResponse<BuyDTO> response = new BoardResponse<>("", true, dto);
        this.template.convertAndSend("/buyEvents", response);
    }

    @EventListener
    public void gameOver(GameOverEvent event) {
        GameOverDTO dto = new GameOverDTO(event.getWinners());
        BoardResponse<GameOverDTO> response = new BoardResponse<>("", true, dto);
        this.template.convertAndSend("/gameOver", response);
    }
}
