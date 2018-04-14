package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.model.Room;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

public interface RoomService {
    Set<Room> getAvailableRooms();
}
