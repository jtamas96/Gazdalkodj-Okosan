package hu.elte.go.dtos;

import java.util.List;

public class RoomDetailsDTO {
    public String name;
    public String uuid;
    public List<PlayerDTO> players;

    public RoomDetailsDTO(String name, String uuid, List<PlayerDTO> players) {
        this.name = name;
        this.uuid = uuid;
        this.players = players;
    }
    public RoomDetailsDTO(){
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
