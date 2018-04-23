package hu.elte.gazdalkodjokosan.data;

import hu.elte.gazdalkodjokosan.data.enums.Item;

import java.util.ArrayList;
import java.util.List;

public class SaleItem { // A mutable representation of Items for each user.

    public String name;
    private boolean available = false;
    boolean purchased = false;

    public SaleItem(Item item){
        name = item.name();
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void enable() {
        available = true;
    }

    public void purchase() {
        purchased = true;
    }

    public boolean isPurchased() { return purchased;}

    public static List<SaleItem> getInitialListForUser(){
        List<SaleItem> result = new ArrayList<>();
        for(Item i: Item.values()){
            result.add(new SaleItem(i));
        }
        return result;
    }
}
