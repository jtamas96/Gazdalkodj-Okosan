package hu.elte.go.dtos;

public class ClientDTO {
    String id;

    public ClientDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
