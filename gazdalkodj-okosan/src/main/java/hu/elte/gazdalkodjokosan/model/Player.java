/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.model;

public class Player {

    private int bankBalance;
    private int cash;
    private int debt;
    private int position;
    private int index;

    public Player(int bankBalance, int cash, int debt, int position, int index) {
        this.bankBalance = bankBalance;
        this.cash = cash;
        this.debt = debt;
        this.position = position;
        this.index = index;
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(int bankBalance) {
        this.bankBalance = bankBalance;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
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
}
