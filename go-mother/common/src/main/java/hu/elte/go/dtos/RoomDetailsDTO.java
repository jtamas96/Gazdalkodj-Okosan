package hu.elte.go.dtos;

import java.util.List;

public class RoomDetailsDTO {
    public String name;
    public String uuid;
    public List<String> players;

    public RoomDetailsDTO(String name, String uuid, List<String> players) {
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

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
