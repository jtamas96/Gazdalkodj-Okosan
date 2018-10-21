package hu.elte.go.dtos;

import java.util.List;

public class ItemListDTO {

    private List<String> itemList;

    public ItemListDTO(List<String> items) {
        this.itemList = items;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public ItemListDTO(){
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }
}
