/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.service;

import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;

import java.util.List;

/**
 *
 * @author sando
 */
public interface BoardService {
    
    // initialise game, return the first player's color in the row
    public BoardResponse<List<Field>> getNewGame(int nrPlayers);
    // return the next player's color in the row
    public BoardResponse<Player> switchToNextPlayer(int playerIndex);
    public BoardResponse<List<Player>> getPlayers();
    public void stepGame();
    public BoardResponse<Player> getPlayer(int playerIndex);
    public BoardResponse<Player> getCurrentPlayer();
    public BoardResponse<List<Item>> buyItems(List<Item> itemList); //TODO: hitelezheto targyak? majd azt is át kell adni.

    public List<Field> getTable();
}
