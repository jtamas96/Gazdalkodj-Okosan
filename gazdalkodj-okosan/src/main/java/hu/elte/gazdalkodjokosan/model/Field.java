/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.model;

import java.util.List;

public class Field {

    private final List<Player> players;
    private final int number;

    public Field(int number, List<Player> players) {
        this.number = number;
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public int getNumber() {
        return number;
    }
}
