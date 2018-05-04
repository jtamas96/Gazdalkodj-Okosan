package hu.elte.gazdalkodjokosan.data;

import hu.elte.gazdalkodjokosan.data.enums.Item;

public interface CardListener {
    public void stepForward(int a);
    public void stepOnBoard(int a);
    public void writeMessage(String s);
    public void transferIncome(int a);
    public void withDrawMoney(int a);
    public void immobilize(int day);
    public void addItemOrGetPrice(Item i, int price);

}
