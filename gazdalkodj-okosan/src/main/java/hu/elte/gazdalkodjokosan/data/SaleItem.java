package hu.elte.gazdalkodjokosan.data;

import hu.elte.gazdalkodjokosan.data.enums.Item;

import java.util.ArrayList;
import java.util.List;

public class SaleItem { // A mutable representation of Items for each user.

    public String name;
    public int cost;
    private boolean available = false;
    boolean purchased = false;

    int reducedPriceWith = 0;

    public SaleItem(Item item) {
        name = item.name();
        cost = item.getCost();
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

    public void reducePriceWith(int val) {
        reducedPriceWith += val;
    }

    public void purchase() {
        purchased = true;
    }

    public void confiscate() {
        purchased = false;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public int getReducedPriceWith() {
        return reducedPriceWith;
    }

    public static List<SaleItem> getInitialListForUser() {
        List<SaleItem> result = new ArrayList<>();
        for (Item i : Item.values()) {
            result.add(new SaleItem(i));
        }
        return result;
    }
}
