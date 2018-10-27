package hu.elte.go.dtos;

import org.springframework.context.ApplicationEvent;

public interface EventConvertible<E extends ApplicationEvent> {
    E toEvent(Object source);
}
