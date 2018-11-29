package hu.elte.go.controller;

import hu.elte.go.BoardResponse;
import hu.elte.go.model.RoomModel;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.events.*;
import hu.elte.go.exceptions.BuyException;
import hu.elte.go.model.GameModel;
import hu.elte.go.model.PlayersModel;
import hu.elte.go.data.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GameController {

    private RoomModel roomModel;
    private PlayersModel playersModel;
    private SimpMessagingTemplate template;
    private ApplicationEventPublisher publisher;

    @Autowired
    public GameController(RoomModel roomModel, PlayersModel playersModel, SimpMessagingTemplate template, ApplicationEventPublisher publisher) {
        this.roomModel = roomModel;
        this.playersModel = playersModel;
        this.publisher = publisher;
        this.template = template;
    }

    @MessageMapping("/step/{roomUuid}/{userUuid}")
    public void stepGame(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        System.out.println(userUuid + " trying to step in room: " + roomUuid);
        String userRoomUuid = roomModel.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            //TODO: Response: User not in this room.
            return;
        }
        Optional<GameModel> optGame = roomModel.getGame(roomUuid);
        if (!optGame.isPresent()) {
            // TODO: Response: game not started in this room.
            return;
        }
        GameModel game = optGame.get();
        game.stepGame(); //Add parameter to this method and check the id.
    }

    @MessageMapping("/newGame/{roomUuid}/{initiatorUuid}")
    @SendTo("/newGameResponse/{roomUuid}")
    public BoardResponse<NewGameStartedDTO> initRoom(@DestinationVariable String roomUuid, @DestinationVariable String initiatorUuid) {
        System.out.println("New game request from " + initiatorUuid + " for room " + roomUuid);
        Optional<Room> optionalRoom = roomModel.getWaitingRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        Room r = optionalRoom.get();
        if (!r.getOwnerUuid().equals(initiatorUuid)) {
            return new BoardResponse<>("Access denied.", false, null);
        }
        if (r.getPlayers().size() < 2) {
            return new BoardResponse<>("Wait for more players.", false, null);
        }
        GameModel game = new GameModel(this.publisher, roomUuid);
        game.newGame(r.getPlayers());
        roomModel.saveGame(r, game);

        List<Player> players = game.getPlayers();
        NewGameStartedDTO response = new NewGameStartedDTO(game.getTable(), players, game.getCurrentPlayer());
        return new BoardResponse<>("", true, response);
    }

    @MessageMapping("/switchPlayer/{roomUuid}/{userUuid}")
    @SendTo("/switchPlayerResponse/{roomUuid}")
    public BoardResponse<PlayerSwitchedDTO> switchPlayer(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        System.out.println(userUuid + " trying to finish his round in room: " + roomUuid);
        String userRoomUuid = roomModel.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            return new BoardResponse<>("Access denied for this room!", false, null);
        }
        Optional<GameModel> optGame = roomModel.getGame(roomUuid);
        if (!optGame.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        GameModel game = optGame.get();
        Player initiator = playersModel.getPlayer(userUuid);
        if (game.getCurrentPlayer().getIndex() == initiator.getIndex()) {
            game.switchPlayer();
            PlayerSwitchedDTO playerDTO = new PlayerSwitchedDTO(game.getCurrentPlayer());
            return new BoardResponse<>("", true, playerDTO);
        } else {
            return new BoardResponse<>("Not your turn!", false, null);
        }
    }

    @MessageMapping("/buyItems/{roomUuid}/{userUuid}")
    @SendTo("/buyItemsResponse/{roomUuid}/{userUuid}")
    public BoardResponse<PurchasedListDTO> buyItems(
            @DestinationVariable String roomUuid, @DestinationVariable String userUuid, ItemListDTO itemsDto) {
        String userRoomUuid = roomModel.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            return new BoardResponse<>("Access denied for this room!", false, null);
        }
        Optional<GameModel> optGame = roomModel.getGame(roomUuid);
        if (!optGame.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        GameModel game = optGame.get();
        try {
            List<Item> wishList = itemsDto.getItemList().stream()
                    .map(Item::valueOf)
                    .collect(Collectors.toList());
            List<Item> bought = game.buyItems(wishList);
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
        if (event.getSource() instanceof GameModel) {
            GameSteppedDTO dto = new GameSteppedDTO(event.getCurrentPlayer(), event.getTable());
            BoardResponse<GameSteppedDTO> response = new BoardResponse<>("", true, dto);
            GameModel gameModel = (GameModel) event.getSource();
            String roomUuid = gameModel.getRoomUuid();
            this.template.convertAndSend("/gameStepped/" + roomUuid, response);
        }
    }

    @EventListener
    public void sendMessage(MessageEvent event) {
        if (event.getSource() instanceof GameModel) {
            MessageDTO dto = new MessageDTO(event.getMessage());
            BoardResponse<MessageDTO> response = new BoardResponse<>("", true, dto);
            GameModel gameModel = (GameModel) event.getSource();
            String roomUuid = gameModel.getRoomUuid();
            this.template.convertAndSend("/messages/" + roomUuid, response);
        }
    }

    @EventListener
    public void updatePlayer(PlayerUpdateEvent event) {
        if (event.getSource() instanceof GameModel) {
            PlayerUpdateDTO dto = new PlayerUpdateDTO(event.getPlayer());
            BoardResponse<PlayerUpdateDTO> response = new BoardResponse<>("", true, dto);
            GameModel gameModel = (GameModel) event.getSource();
            String roomUuid = gameModel.getRoomUuid();
            this.template.convertAndSend("/playerUpdates/" + roomUuid, response);
        }
    }

    @EventListener
    public void buyEvent(BuyEvent event) {
        if (event.getSource() instanceof GameModel) {
            BuyDTO dto = new BuyDTO(event.getPlayer(), event.getItemPrices());
            BoardResponse<BuyDTO> response = new BoardResponse<>("", true, dto);
            GameModel gameModel = (GameModel) event.getSource();
            String roomUuid = gameModel.getRoomUuid();
            this.template.convertAndSend("/buyEvents/" + roomUuid, response);
        }
    }

    @EventListener
    public void gameOver(GameOverEvent event) {
        if (event.getSource() instanceof GameModel) {
            GameOverDTO dto = new GameOverDTO(event.getWinners());
            BoardResponse<GameOverDTO> response = new BoardResponse<>("", true, dto);
            GameModel gameModel = (GameModel) event.getSource();
            String roomUuid = gameModel.getRoomUuid();
            this.template.convertAndSend("/gameOver/" + roomUuid, response);
        }
    }
}
