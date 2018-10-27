package hu.elte.go.controller;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.events.*;
import hu.elte.go.exceptions.BuyException;
import hu.elte.go.exceptions.PlayerNumberException;
import hu.elte.go.model.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public BoardResponse<PurchasedListDTO> buyItems(ItemListDTO itemsDto) {
        try {
            List<Item> wishList = itemsDto.getItemList().stream()
                    .map(Item::valueOf)
                    .collect(Collectors.toList());
            List<Item> bought = gameModel.buyItems(wishList);
            Map<String, Integer> boughtMap = bought.stream()
                    .collect(Collectors.toMap(Enum::toString, Item::getCost));
            PurchasedListDTO responseDto = new PurchasedListDTO(boughtMap);
            return new BoardResponse<>("", true, responseDto);
        } catch (BuyException | IllegalArgumentException ex) {
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
    public void updatePlayer(PlayerUpdateEvent event) {
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
