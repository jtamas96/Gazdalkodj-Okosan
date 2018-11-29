package hu.elte.go.model;

import hu.elte.go.data.Player;
import hu.elte.go.data.Room;
import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.dtos.RoomListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;

@Component
public class RoomModel {

    private ConcurrentMap<Room, GameModel> roomsMapToGameModel;
    private ConcurrentMap<String, String> usersMapToRooms;
    private ApplicationEventPublisher publisher;
    private PlayersModel playersModel;

    @Autowired
    public RoomModel(PlayersModel playersModel, ApplicationEventPublisher publisher) {
        roomsMapToGameModel = new ConcurrentHashMap<>();
        usersMapToRooms = new ConcurrentHashMap<>();
        this.playersModel = playersModel;
        this.publisher = publisher;
    }

    public void createRoom(@NonNull Room room) {
        roomsMapToGameModel.put(room, new GameModel(publisher, room.getUuid()));
    }

    public Optional<Room> getRoom(String uuid) {
        return roomsMapToGameModel.keySet().stream()
                .filter(r -> !r.isGameStarted() && r.getUuid().equals(uuid))
                .findFirst();
    }

    public Optional<GameModel> getGame(String uuid) {
        Optional<Room> optionalRoom = roomsMapToGameModel.keySet().stream()
                .filter(r -> r.getUuid().equals(uuid))
                .findFirst();
        return optionalRoom.map(room -> roomsMapToGameModel.get(room));
    }

    public RoomListDTO toRoomListDTO() {
        RoomListDTO result = new RoomListDTO(new ArrayList<>());
        roomsMapToGameModel.keySet().stream().filter(r -> !r.isGameStarted()).forEach((room) -> {
            List<String> playerNames = room.getPlayers().stream()
                    .map(player -> player.getName())
                    .collect(Collectors.toList());
            result.rooms.add(new RoomDetailsDTO(room.getName(), room.getUuid(), playerNames));
        });
        return result;
    }

    public void deleteRoom(Room room) {
        roomsMapToGameModel.remove(room);
    }

    public void mapUserToRoom(String userUuid, String roomUuid) {
        usersMapToRooms.putIfAbsent(userUuid, roomUuid);
    }

    public String getUserRoom(String userUuid) {
        return usersMapToRooms.get(userUuid);
    }

    public void removeUserFromRoom(String userUuid, String roomUuid) {
        usersMapToRooms.remove(userUuid);
        Optional<Room> optionalCurrentRoom = roomsMapToGameModel.keySet().stream()
                .filter(r -> r.getUuid().equals(roomUuid)).findFirst();
        if (optionalCurrentRoom.isPresent()) {
            Room room = optionalCurrentRoom.get();
            Player player = playersModel.getPlayer(userUuid);
            room.removePlayer(player);
        }
    }

    public Room getOwnedRoom(String userUuid) {
        Optional<Room> optionalRoom = roomsMapToGameModel.keySet().stream()
                .filter(r -> r.getOwnerUuid().equals(userUuid)).findFirst();
        if (optionalRoom.isPresent()) {
            return optionalRoom.get();
        }
        return null;
    }
}
