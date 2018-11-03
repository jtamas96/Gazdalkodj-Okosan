package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

public class ErrorEvent extends ApplicationEvent {
    public String message;
    public ErrorEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
