package hu.elte.go.controller;

import hu.elte.go.ActivePlayers;
import hu.elte.go.BoardResponse;
import hu.elte.go.RoomsMapping;
import hu.elte.go.data.Player;
import hu.elte.go.dtos.RoomCreationDTO;
import hu.elte.go.dtos.RoomDeletionDTO;
import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.dtos.RoomListDTO;
import hu.elte.go.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RoomController {
    private RoomsMapping roomsMapping;
    private ActivePlayers playersMapping;

    @Autowired
    public RoomController(RoomsMapping roomsMapping, ActivePlayers playersMapping) {
        this.roomsMapping = roomsMapping;
        this.playersMapping = playersMapping;
    }

    @MessageMapping("/rooms")
    @SendTo("/roomsResponse")
    public BoardResponse<RoomListDTO> getRooms() {
        return new BoardResponse<>("", true, roomsMapping.toRoomListDTO());
    }

    @MessageMapping("/createRoom/{ownerUuid}")
    @SendTo("/createRooResponse/{ownerUuid}")
    public BoardResponse<RoomCreationDTO> createRoom(@DestinationVariable String ownerUuid, String name) {
        Player owner = playersMapping.getPlayer(ownerUuid);
        if (owner == null) {
            return new BoardResponse<>("Player does not exist.", false, null);
        }
        String roomUuid = UUID.randomUUID().toString();
        List<Player> players = new ArrayList<>();
        players.add(owner);
        Room newRoom = new Room(roomUuid, name, ownerUuid, players);
        roomsMapping.saveRoom(newRoom, null);
        RoomCreationDTO dto = new RoomCreationDTO(name, ownerUuid, roomUuid);
        return new BoardResponse<RoomCreationDTO>("", true, dto);
    }

    @MessageMapping("/joinRoom/{roomUuid}/{userUuid}")
    @SendTo("/joinRooResponse/{userUuid}")
    public BoardResponse<RoomDetailsDTO> joinRoom(@DestinationVariable String roomUuid, @DestinationVariable String userUuid) {
        Player player = playersMapping.getPlayer(userUuid);
        if (player == null) {
            return new BoardResponse<>("Player not exist.", false, null);
        }
        String userRoomUuid = roomsMapping.getUserRoom(userUuid);
        if(userRoomUuid != null){
            return new BoardResponse<>(
                    "Player already joined ro room with uuid " + userRoomUuid,
                    false, null);
        }
        Optional<Room> optionalRoom = roomsMapping.getRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        Room r = optionalRoom.get();
        if (r.getPlayers().size() >= 6) {
            return new BoardResponse<>("Room is full.", false, null);
        }
        r.getPlayers().add(player);
        roomsMapping.mapUserToRoom(userUuid, roomUuid);
        return new BoardResponse<>("", true, r.roomDTO());
    }

    @MessageMapping("/deleteRoom/{roomUuid}/{initiatorUuid}")
    @SendTo("/deleteRooResponse/{initiatorUuid}")
    public BoardResponse<RoomDeletionDTO> deleteRoom(@DestinationVariable String roomUuid, @DestinationVariable String initiatorUuid) {
        Optional<Room> optionalRoom = roomsMapping.getRoom(roomUuid);
        if (!optionalRoom.isPresent()) {
            return new BoardResponse<>("Room not exist.", false, null);
        }
        Room r = optionalRoom.get();
        if (!r.getOwnerUuid().equals(initiatorUuid)) {
            return new BoardResponse<>("Access denied.", false, null);
        }
        roomsMapping.deleteRoom(r);
        return new BoardResponse<>("", true, new RoomDeletionDTO());
    }
}
