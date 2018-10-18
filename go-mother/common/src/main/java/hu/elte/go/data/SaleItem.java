package hu.elte.go.data;

import hu.elte.go.data.enums.Item;

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

    public int getCost() {
        return cost;
    }

    public boolean isPurchased() {
        return purchased;
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

    public int getReducedPriceWith() {
        return reducedPriceWith;
    }
    public int reducedPrice(){
        //TODO: use this function where difference is needed. andor 2018-10-16
        return cost - reducedPriceWith;
    }

    public static List<SaleItem> getInitialListForUser() {
        List<SaleItem> result = new ArrayList<>();
        for (Item i : Item.values()) {
            result.add(new SaleItem(i));
        }
        return result;
    }
    public SaleItem(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public void setReducedPriceWith(int reducedPriceWith) {
        this.reducedPriceWith = reducedPriceWith;
    }
}
