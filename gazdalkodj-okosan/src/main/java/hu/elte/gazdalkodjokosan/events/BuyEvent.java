package hu.elte.gazdalkodjokosan.events;

import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class BuyEvent extends ApplicationEvent {
    Player player;
    Map<Item, Boolean> purchaseAble;

    public BuyEvent(Object source, Player player, Map<Item, Boolean> purchaseAble) {
        super(source);
        this.player = player;
        this.purchaseAble = purchaseAble;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<Item, Boolean> getPurchaseAble() {
        return purchaseAble;
    }
}
