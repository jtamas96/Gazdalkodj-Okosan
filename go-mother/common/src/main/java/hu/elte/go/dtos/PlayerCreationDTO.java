package hu.elte.go.dtos;

import hu.elte.go.events.PlayerCreatedEvent;

public class PlayerCreationDTO implements EventConvertible<PlayerCreatedEvent> {
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

    @Override
    public PlayerCreatedEvent toEvent(Object source) {
        return new PlayerCreatedEvent(source);
    }
}
