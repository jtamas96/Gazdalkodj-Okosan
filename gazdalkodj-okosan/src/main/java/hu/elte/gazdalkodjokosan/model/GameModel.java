/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.model;

import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

    List<Player> players;
    List<Field> table;

    public GameModel() {

    }

    public void newGame(int playerNumber) throws PlayerNumberException {
        if (playerNumber >= 2 || playerNumber <= 6) {
            players = new ArrayList<>();
            for (int i = 0; i < playerNumber; i++) {
                players.add(new Player(3000000, 238000, 0, 0, i));
            }
            table = new ArrayList<>();
            table.add(new Field(0, players));
            for (int i = 1; i < 42; i++) {
                table.add(new Field(i, new ArrayList<>()));
            }
        } else {
            throw new PlayerNumberException("Invalid number of players.");
        }
    }
}
