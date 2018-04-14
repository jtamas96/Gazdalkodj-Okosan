package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.model.Room;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class HardcodedListRoomService implements RoomService {
    @Override
    public Set<Room> getAvailableRooms() {
        Set<Room> result = new HashSet<>();
        result.add(new Room(1L, "Bajnok"));
        result.add(new Room(2L, "Csicska"));
        return result;
    }
}