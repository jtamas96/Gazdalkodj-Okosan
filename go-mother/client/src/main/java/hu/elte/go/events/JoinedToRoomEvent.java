package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

public class JoinedToRoomEvent extends ApplicationEvent {
    public JoinedToRoomEvent(Object source) {
        super(source);
    }
}
