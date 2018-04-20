package hu.elte.gazdalkodjokosan.model;

import hu.elte.gazdalkodjokosan.model.enums.Item;

import java.util.ArrayList;
import java.util.List;

public class SaleItem {
    private int cost;
    private boolean creditable;
    private boolean available = false;
    boolean purchased = false;
    public Integer creditableTotalCost;
    public Integer creditableStartCost;
    public Integer annualCost;

    public SaleItem(Item item){
        cost = item.getCost();
        creditable = item.isCreditable();
        creditableTotalCost = item.creditableTotalCost;
        creditableStartCost = item.creditableStartCost;
        annualCost = item.annualCost;
    }

    public int getCost() {
        return cost;
    }

    public boolean isAvailable() {
        return available;
    }

    public void enable() {
        available = true;
    }

    public boolean isCreditable() {
        return creditable;
    }
    public void purchase() {
        purchased = true;
    }

    public static List<SaleItem> getInitialListForUser(){
        List<SaleItem> result = new ArrayList<SaleItem>();
        for(Item i: Item.values()){
            result.add(new SaleItem(i));
        }
        return result;
    }
}
