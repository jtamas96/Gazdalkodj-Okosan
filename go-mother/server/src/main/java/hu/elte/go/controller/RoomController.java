package hu.elte.go.controller;

import hu.elte.go.BoardResponse;
import hu.elte.go.model.RoomModel;
import hu.elte.go.data.Player;
import hu.elte.go.dtos.RoomCreationDTO;
import hu.elte.go.dtos.RoomDeletionDTO;
import hu.elte.go.dtos.RoomListDTO;
import hu.elte.go.model.PlayersModel;
import hu.elte.go.data.Room;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class RoomController {

    private RoomModel roomModel;
    private PlayersModel playersModel;
    private SimpMessagingTemplate template;

    @Autowired
    public RoomController(RoomModel roomModel, PlayersModel playersModel, SimpMessagingTemplate template) {
        this.roomModel = roomModel;
        this.playersModel = playersModel;
        this.template = template;
    }

    @MessageMapping("/rooms")
    @SendTo("/roomsResponse")
    public BoardResponse<RoomListDTO> getRooms() {
        System.out.println("Room list request.");
        return new BoardResponse<>("", true, roomModel.toRoomListDTO());
    }

    @MessageMapping("/createRoom/{ownerUuid}")
    @SendTo("/createRoomResponse/{ownerUuid}")
    public BoardResponse<RoomCreationDTO> createRoom(@DestinationVariable String ownerUuid, String name) {
        Player owner = playersModel.getPlayer(ownerUuid);
        if (owner == null) {
            return new BoardResponse<>("Nem létező játékos.", false, null);
        }
        String roomUuid = UUID.randomUUID().toString();
        while (roomModel.getRoom(roomUuid).isPresent()) {
            roomUuid = UUID.randomUUID().toString();
        }
        String currentRoomUuid = roomModel.getUserRoom(ownerUuid);
        if (roomModel.getOwnedRoom(ownerUuid) != null) {
            return new BoardResponse("Csak egy szoba létrehozása megengedett felhasználónként!", false, null);
        } else if (currentRoomUuid != null) {
            roomModel.removeUserFromRoom(ownerUuid, currentRoomUuid);
        }
        List<Player> players = Collections.synchronizedList(new ArrayList<>());
        players.add(owner);
        Room newRoom = new Room(roomUuid, name, ownerUuid, players);
        roomModel.createRoom(newRoom);
        roomModel.mapUserToRoom(ownerUuid, roomUuid);
        RoomCreationDTO dto = new RoomCreationDTO(name, roomUuid);
        template.convertAndSend("/roomsResponse", new BoardResponse<>("", true, roomModel.toRoomListDTO()));
        return new BoardResponse<>("", true, dto);
    }

    @MessageMapping("/joinRoom/{roomUuid}/{userUuid}")
    @SendTo("/joinRoomResponse/{userUuid}")
    public BoardResponse<Void> joinRoom(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        Player player = playersModel.getPlayer(userUuid);
        if (player == null) {
            return new BoardResponse<>("Nem létező játékos.", false, null);
        }
        String userRoomUuid = roomModel.getUserRoom(userUuid);
        if (userRoomUuid != null) {
            return new BoardResponse<>(
                    "Már csatlakozott játékos a szobához ehhez a uuid-vel. " + userRoomUuid,
                    false, null);
        }
        Optional<Room> optionalRoom = roomModel.getRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("A szoba nem létezik.", false, null);
        }
        Room r = optionalRoom.get();
        if (r.getPlayers().size() >= 6) {
            return new BoardResponse<>("A szoba tele van.", false, null);
        }
        String currentRoomUuid = roomModel.getUserRoom(userUuid);
        if (currentRoomUuid != null) {
            roomModel.removeUserFromRoom(userUuid, currentRoomUuid);
        }
        r.getPlayers().add(player);
        roomModel.mapUserToRoom(userUuid, roomUuid);
        template.convertAndSend("/roomsResponse", new BoardResponse<>("", true, roomModel.toRoomListDTO()));
        return new BoardResponse<>("", true, null);
    }

    @MessageMapping("/deleteRoom/{roomUuid}/{initiatorUuid}")
    @SendTo("/deleteRoomResponse/{initiatorUuid}")
    public BoardResponse<RoomDeletionDTO> deleteRoom(@DestinationVariable String roomUuid, @DestinationVariable String initiatorUuid) {
        Optional<Room> optionalRoom = roomModel.getRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("A szoba nem létezik.", false, null);
        }
        Room r = optionalRoom.get();
        if (!r.getOwnerUuid().equals(initiatorUuid)) {
            return new BoardResponse<>("Hozzáférés megtagadva.", false, null);
        }
        roomModel.deleteRoom(r);
        template.convertAndSend("/roomsResponse", new BoardResponse<>("", true, roomModel.toRoomListDTO()));
        return new BoardResponse<>("", true, new RoomDeletionDTO());
    }
}
