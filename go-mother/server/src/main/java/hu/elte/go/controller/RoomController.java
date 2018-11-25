package hu.elte.go.controller;

import hu.elte.go.BoardResponse;
import hu.elte.go.model.RoomModel;
import hu.elte.go.data.Player;
import hu.elte.go.dtos.RoomCreationDTO;
import hu.elte.go.dtos.RoomDeletionDTO;
import hu.elte.go.dtos.RoomDetailsDTO;
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

@Controller
public class RoomController {
    private RoomModel roomModel;
    private PlayersModel playersModel;

    @Autowired
    public RoomController(RoomModel roomModel, PlayersModel playersModel) {
        this.roomModel = roomModel;
        this.playersModel = playersModel;
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
            return new BoardResponse<>("Player does not exist.", false, null);
        }
        String roomUuid = UUID.randomUUID().toString();
        List<Player> players = Collections.synchronizedList( new ArrayList<>());
        players.add(owner);
        Room newRoom = new Room(roomUuid, name, ownerUuid, players);
        roomModel.createRoom(newRoom);
        roomModel.mapUserToRoom(ownerUuid, roomUuid);
        RoomCreationDTO dto = new RoomCreationDTO(name, roomUuid);
        return new BoardResponse<>("", true, dto);
    }

    @MessageMapping("/joinRoom/{roomUuid}/{userUuid}")
    @SendTo("/joinRooResponse/{userUuid}")
    public BoardResponse<Void> joinRoom(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        Player player = playersModel.getPlayer(userUuid);
        if (player == null) {
            return new BoardResponse<>("Player not exist.", false, null);
        }
        String userRoomUuid = roomModel.getUserRoom(userUuid);
        if(userRoomUuid != null){
            return new BoardResponse<>(
                    "Player already joined to room with uuid " + userRoomUuid,
                    false, null);
        }
        Optional<Room> optionalRoom = roomModel.getWaitingRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        Room r = optionalRoom.get();
        if (r.getPlayers().size() >= 6) {
            return new BoardResponse<>("Room is full.", false, null);
        }
        r.getPlayers().add(player);
        roomModel.mapUserToRoom(userUuid, roomUuid);
        return new BoardResponse<>("", true, null);
    }

    @MessageMapping("/deleteRoom/{roomUuid}/{initiatorUuid}")
    @SendTo("/deleteRooResponse/{initiatorUuid}")
    public BoardResponse<RoomDeletionDTO> deleteRoom(@DestinationVariable String roomUuid, @DestinationVariable String initiatorUuid) {
        Optional<Room> optionalRoom = roomModel.getWaitingRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        Room r = optionalRoom.get();
        if (!r.getOwnerUuid().equals(initiatorUuid)) {
            return new BoardResponse<>("Access denied.", false, null);
        }
        roomModel.deleteRoom(r);
        return new BoardResponse<>("", true, new RoomDeletionDTO());
    }
}
