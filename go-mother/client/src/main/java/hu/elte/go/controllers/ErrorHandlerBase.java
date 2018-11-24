package hu.elte.go.controllers;

import hu.elte.go.events.ErrorEvent;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.ResourceBundle;

public interface ErrorHandlerBase {
    default void handleError(ErrorEvent event) {
        Notifications notification = Notifications.create()
                .title(ResourceBundle.getBundle("Bundle").getString(("window.username.error.title")))
                .text(event.message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.CENTER);
        notification.showError();
    }
}
