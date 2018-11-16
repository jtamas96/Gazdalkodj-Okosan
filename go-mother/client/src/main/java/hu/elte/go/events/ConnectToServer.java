package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

public class ConnectToServer extends ApplicationEvent {
    private boolean successful;

    public ConnectToServer(Object source, boolean successful) {
        super(source);
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
