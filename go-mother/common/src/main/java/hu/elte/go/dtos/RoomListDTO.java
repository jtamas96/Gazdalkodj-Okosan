package hu.elte.go.dtos;

import java.util.List;

public class RoomListDTO {
    public List<RoomDetailsDTO> rooms;

    public RoomListDTO(List<RoomDetailsDTO> rooms) {
        this.rooms = rooms;
    }

    public void setRooms(List<RoomDetailsDTO> rooms) {
        this.rooms = rooms;
    }
}
