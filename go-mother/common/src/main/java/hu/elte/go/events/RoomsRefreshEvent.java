package hu.elte.go.events;

import hu.elte.go.dtos.RoomDetailsDTO;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class RoomsRefreshEvent extends ApplicationEvent {
    private List<RoomDetailsDTO> rooms;

    public RoomsRefreshEvent(Object source, List<RoomDetailsDTO> rooms) {
        super(source);
        this.rooms = rooms;
    }

    public List<RoomDetailsDTO> getRooms() {
        return rooms;
    }
}
