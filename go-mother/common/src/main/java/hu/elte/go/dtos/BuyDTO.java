package hu.elte.go.dtos;

import hu.elte.go.data.Player;

import java.util.Map;

public class BuyDTO {
    Player player;
    Map<String, Integer> itemPrices;

    public BuyDTO(Player player, Map<String, Integer> itemPrices) {
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
