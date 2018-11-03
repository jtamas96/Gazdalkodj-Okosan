package hu.elte.go.controller;

import hu.elte.go.ActivePlayers;
import hu.elte.go.BoardResponse;
import hu.elte.go.RoomsMapping;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import hu.elte.go.events.*;
import hu.elte.go.exceptions.BuyException;
import hu.elte.go.model.GameModel;
import hu.elte.go.model.Room;
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

    private RoomsMapping roomsMapping;
    private ActivePlayers playersMapping;
    private SimpMessagingTemplate template;
    private ApplicationEventPublisher publisher;

    @Autowired
    public GameController(RoomsMapping roomsMapping, ActivePlayers playersMapping, SimpMessagingTemplate template, ApplicationEventPublisher publisher) {
        this.roomsMapping = roomsMapping;
        this.playersMapping = playersMapping;
        this.publisher = publisher;
        this.template = template;
    }

    @MessageMapping("/step/{roomUuid}/{userUuid}")
    public void stepGame(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        String userRoomUuid = roomsMapping.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            //TODO: Response: User not in this room.
            return;
        }
        Optional<GameModel> optGame = roomsMapping.getGame(roomUuid);
        if (!optGame.isPresent()) {
            // TODO: Response: game not started in this room.
            return;
        }
        GameModel game = optGame.get();
        game.stepGame(); //Add parameter to this method and check the id.
    }

    @MessageMapping("/newGame/{roomUuid}/{initiatorUuid}")
    @SendTo("/newGameResponse/{roomUuid}/{initiatorUuid}")
    public BoardResponse<NewGameStartedDTO> initRoom(@DestinationVariable String roomUuid, @DestinationVariable String initiatorUuid) {
        Optional<Room> optionalRoom = roomsMapping.getRoom(roomUuid);
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
        GameModel game = new GameModel(this.publisher);
        game.newGame(r.getPlayers());
        roomsMapping.saveRoom(r, game);

        List<Player> players = game.getPlayers();
        NewGameStartedDTO response = new NewGameStartedDTO(game.getTable(), players, game.getCurrentPlayer());
        return new BoardResponse<>("", true, response);
    }

    @MessageMapping("/switchPlayer/{roomUuid}/{userUuid}")
    @SendTo("/switchPlayerResponse/{roomUuid}/{userUuid}")
    public BoardResponse<PlayerSwitchedDTO> switchPlayer(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        String userRoomUuid = roomsMapping.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            return new BoardResponse<>("Access denied for this room!", false, null);
        }
        Optional<GameModel> optGame = roomsMapping.getGame(roomUuid);
        if (!optGame.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        GameModel game = optGame.get();
        Player initiator = playersMapping.getPlayer(userUuid);
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
        String userRoomUuid = roomsMapping.getUserRoom(userUuid);
        if (!roomUuid.equals(userRoomUuid)) {
            return new BoardResponse<>("Access denied for this room!", false, null);
        }
        Optional<GameModel> optGame = roomsMapping.getGame(roomUuid);
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
