package hu.elte.go.dtos;

public class RoomCreationDTO {
    public String name;
    public String roomUuid;

    public RoomCreationDTO(){
    }

    public RoomCreationDTO(String name, String roomUuid) {
        this.name = name;
        this.roomUuid = roomUuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomUuid(String roomUuid) {
        this.roomUuid = roomUuid;
    }
}
