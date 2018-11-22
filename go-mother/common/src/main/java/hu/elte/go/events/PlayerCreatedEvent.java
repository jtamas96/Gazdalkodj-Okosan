package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

public class PlayerCreatedEvent extends ApplicationEvent {

    public PlayerCreatedEvent(Object source) {
        super(source);
    }
}
