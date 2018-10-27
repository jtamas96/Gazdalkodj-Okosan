package hu.elte.go.dtos;

import hu.elte.go.events.ItemsPurchasedEvent;

import java.util.Map;

public class PurchasedListDTO implements EventConvertible<ItemsPurchasedEvent> {
    private Map<String, Integer> itemMap;

    public PurchasedListDTO(Map<String, Integer> itemMap) {

        this.itemMap = itemMap;
    }

    public PurchasedListDTO() {
    }

    public Map<String, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }


    @Override
    public ItemsPurchasedEvent toEvent(Object source) { ;
        return new ItemsPurchasedEvent(source, itemMap);
    }
}
