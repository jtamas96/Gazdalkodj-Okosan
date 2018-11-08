package hu.elte.go.model;

import hu.elte.go.data.Room;
import hu.elte.go.dtos.PlayerDTO;
import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.dtos.RoomListDTO;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
public class RoomModel {
    private ConcurrentMap<Room, GameModel> roomsMapToGameModel;
    private ConcurrentMap<String, String> usersMapToRooms;

    @Autowired
    public RoomModel() {
        this.roomsMapToGameModel = new ConcurrentHashMap<>();
        this.usersMapToRooms = new ConcurrentHashMap<>();
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
        roomsMapToGameModel.keySet().forEach((room) -> {
            GameModel g = roomsMapToGameModel.get(room);
            List<String> playerNames = g.getPlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
            result.rooms.add(new RoomDetailsDTO(room.getName(), room.getUuid(), playerNames));
        });
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
