package hu.elte.go.dtos;

import java.util.Map;

public class PurchasedListDTO {
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
}
