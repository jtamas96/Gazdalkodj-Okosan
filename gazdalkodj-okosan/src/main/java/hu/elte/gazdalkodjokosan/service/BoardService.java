/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.service;

import hu.elte.gazdalkodjokosan.common.transfer.*;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;

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
    public BoardResponse<String> buyCar(int playerIndex, boolean loan);
    public BoardResponse<String> buyHouse(int playerIndex, boolean loan);
    public BoardResponse<String> buyInsurance(int playerIndex, Insurance insurance);
    public BoardResponse<String> buyHouseAsset(int playerIndex, HouseAsset houseAsset);
    public BoardResponse<String> buyBKVPass(int playerIndex);

    public List<Field> getTable();
}
