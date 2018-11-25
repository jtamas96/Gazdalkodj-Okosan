package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

public class RoomCreatedEvent extends ApplicationEvent {
    private String name;
    private String uuid;

    public RoomCreatedEvent(Object source, String name, String uuid) {
        super(source);
        this.name = name;
        this.uuid = uuid;
    }

    public RoomCreatedEvent(Object source, RoomCreatedEvent other) {
        super(source);
        this.name = other.name;
        this.uuid = other.uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
