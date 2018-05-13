/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.data;

import java.util.List;

public class Field {

    private final List<Player> playersOnFiled;
    private final int number;

    public Field(int number, List<Player> players) {
        this.number = number;
        this.playersOnFiled = players;
    }

    public List<Player> getPlayers() {
        return playersOnFiled;
    }

    public void addPlayer(Player player) {
        playersOnFiled.add(player);
    }

    public void removePlayer(Player player) {
        playersOnFiled.remove(player);
    }

    public int getNumber() {
        return number;
    }
}
