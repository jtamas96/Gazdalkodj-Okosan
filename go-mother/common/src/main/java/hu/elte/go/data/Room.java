package hu.elte.go.data;

import hu.elte.go.dtos.RoomDetailsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Room implements Comparable<Room> {

    private String uuid;
    private String name;
    private String ownerUuid;
    private List<Player> players;
    private boolean isGameStarted;

    public Room(String uuid, String name, String ownerUuid, List<Player> players) {
        this.uuid = uuid;
        this.name = name;
        this.ownerUuid = ownerUuid;
        this.players = players;
        isGameStarted = false;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public RoomDetailsDTO roomDTO() {
        List<String> playerNames = players.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());
        return new RoomDetailsDTO(name, uuid, playerNames);
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setIsGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public int hashCode() {
        return UUID.fromString(uuid).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Room)) {
            return false;
        }
        Room o = (Room) obj;
        if (this.uuid == null || o.getUuid() == null) {
            return false;
        }
        return this.uuid.equals(o.getUuid());
    }

    @Override
    public int compareTo(Room o) {
        return uuid.compareTo(o.uuid);
    }
}
