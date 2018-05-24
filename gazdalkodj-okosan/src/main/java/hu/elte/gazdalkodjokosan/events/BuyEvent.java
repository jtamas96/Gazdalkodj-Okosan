package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class BuyEvent extends ApplicationEvent {
    Player player;
    Map<String, Integer> itemPrices;

    public BuyEvent(Object source, Player player, Map<String, Integer> itemPrices) {
        super(source);
        this.player = player;
        this.itemPrices = itemPrices;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Integer> getItemPrices() {
        return itemPrices;
    }
}
