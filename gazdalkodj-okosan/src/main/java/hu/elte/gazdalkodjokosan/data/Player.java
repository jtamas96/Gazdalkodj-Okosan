package hu.elte.gazdalkodjokosan.data;

import hu.elte.gazdalkodjokosan.common.transfer.Insurance;
import hu.elte.gazdalkodjokosan.data.enums.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Player {

    private int bankBalance;
    private int debt;
    private int position;
    private int index;
    private List<SaleItem> items;
    private boolean winner;
    private boolean withHouse;
    private boolean withCar;
    private boolean withBKVPass;
    private final Set<Insurance> insurances;

    public Player(int bankBalance, int debt, int position, int index, List<SaleItem> items) {
        this.bankBalance = bankBalance;
        this.debt = debt;
        this.position = position;
        this.index = index;
        this.items = items;
        winner = false;
        withHouse = false;
        withCar = false;
        withBKVPass = false;
        insurances = new HashSet<>();
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(int bankBalance) {
        this.bankBalance = bankBalance;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isWithHouse() {
        return withHouse;
    }

    public boolean isWithCar() {
        return withCar;
    }

    public boolean isWithBKVPass() {
        return withBKVPass;
    }

    public Set<Insurance> getInsurances() {
        return insurances;
    }

    public Optional<SaleItem> getItem(Item it){
        return items.stream()
                .filter(item -> item.name.equals(it.name()))
                .findFirst();
    }
}
