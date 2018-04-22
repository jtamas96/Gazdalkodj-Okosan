/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.service;

import java.util.Set;

/**
 *
 * @author sando
 */
public class PlayerStatus {
    
    private final boolean winner;
    private final int cash;
    private final int balance;
    private final int boardIndex;
    private final boolean withHouse;
    private final boolean withCar;
    private final boolean withBKVPass;
    private final Set<Insurance> insurances;
    private final Set<HouseAsset> houseAssets;

    public PlayerStatus(boolean winner, int cash, int balance, int boardIndex, boolean withHouse, boolean withCar, boolean withBKVPass, Set<Insurance> insurances, Set<HouseAsset> houseAssets) {
        this.winner = winner;
        this.cash = cash;
        this.balance = balance;
        this.boardIndex = boardIndex;
        this.withHouse = withHouse;
        this.withCar = withCar;
        this.withBKVPass = withBKVPass;
        this.insurances = insurances;
        this.houseAssets = houseAssets;
    }

    public boolean isWinner() {
        return winner;
    }

    public int getCash() {
        return cash;
    }

    public int getBalance() {
        return balance;
    }

    public int getBoardIndex() {
        return boardIndex;
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

    public Set<HouseAsset> getHouseAssets() {
        return houseAssets;
    }
}
