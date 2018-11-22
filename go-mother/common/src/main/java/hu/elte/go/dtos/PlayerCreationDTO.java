package hu.elte.go.dtos;

public class PlayerCreationDTO {
    public String name;
    public String uuid;

    public PlayerCreationDTO(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
    public PlayerCreationDTO(){
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
