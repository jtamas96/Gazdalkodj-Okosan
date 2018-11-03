package hu.elte.go.dtos;

public class RoomCreationDTO {
    public String name;
    public String ownerUuid;
    public String roomUuid;

    public RoomCreationDTO(String name, String ownerUuid, String roomUuid) {
        this.name = name;
        this.ownerUuid = ownerUuid;
        this.roomUuid = roomUuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public void setRoomUuid(String roomUuid) {
        this.roomUuid = roomUuid;
    }
}
