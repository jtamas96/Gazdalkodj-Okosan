package hu.elte.go;

import hu.elte.go.dtos.PlayerDTO;
import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.dtos.RoomListDTO;;
import hu.elte.go.model.GameModel;
import hu.elte.go.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RoomsMapping {
    private Map<Room, GameModel> roomsMapToGameModel;
    private Map<String, String> usersMapToRooms;

    @Autowired
    public RoomsMapping() {
        this.roomsMapToGameModel = new HashMap<>();
        this.usersMapToRooms = new HashMap<>();
    }

    public void saveRoom(Room room, GameModel game) {
        roomsMapToGameModel.put(room, game);
    }

    public Optional<Room> getRoom(String uuid){
        return roomsMapToGameModel.keySet().stream()
                .filter(r -> r.getUuid().equals(uuid))
                .findFirst();
    }

    public Optional<GameModel> getGame(String uuid){
        Optional<Room> optionalRoom = roomsMapToGameModel.keySet().stream()
                .filter(r -> r.getUuid().equals(uuid))
                .findFirst();
        return optionalRoom.map(room -> roomsMapToGameModel.get(room));
    }

    public RoomListDTO toRoomListDTO(){
        RoomListDTO result = new RoomListDTO(new ArrayList<>());
        for (Room room: roomsMapToGameModel.keySet()) {
            GameModel g = roomsMapToGameModel.get(room);
            List<PlayerDTO> players = g.getPlayers().stream()
                    .map(player -> new PlayerDTO(player.name))
                    .collect(Collectors.toList());
            result.rooms.add(new RoomDetailsDTO(room.getName(), room.getUuid(), players));
        }
        return result;
    }

    public void deleteRoom(Room room) {
        roomsMapToGameModel.remove(room);
    }

    public void mapUserToRoom(String userUuid, String roomUuid){
        this.usersMapToRooms.putIfAbsent(userUuid, roomUuid);
    }

    public String getUserRoom(String userUuid) {
        return this.usersMapToRooms.get(userUuid);
    }
}
