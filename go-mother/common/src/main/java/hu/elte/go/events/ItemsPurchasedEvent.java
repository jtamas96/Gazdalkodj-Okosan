package hu.elte.go.events;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class ItemsPurchasedEvent extends ApplicationEvent {
    private Map<String, Integer> items;

    public ItemsPurchasedEvent(Object source, Map<String, Integer> items) {
        super(source);
        this.items = items;
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}
