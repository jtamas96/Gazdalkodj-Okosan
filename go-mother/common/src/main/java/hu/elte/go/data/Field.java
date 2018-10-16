/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.data;

import java.util.List;

public class Field {

    private List<Player> playersOnFiled;
    private int number;

    public Field(int number, List<Player> playersOnFiled) {
        this.number = number;
        this.playersOnFiled = playersOnFiled;
    }

    public List<Player> getPlayersOnFiled() {
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

    public void setPlayersOnFiled(List<Player> playersOnFiled) {
        this.playersOnFiled = playersOnFiled;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Field(){

    }
}
