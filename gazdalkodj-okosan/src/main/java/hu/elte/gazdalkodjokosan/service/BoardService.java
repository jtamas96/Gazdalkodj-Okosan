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
    public BoardResponse<Integer> getNewGame(int nrPlayers);
    // return the next player's color in the row
    public BoardResponse<Integer> switchToNextPlayer(int playerIndex);
    public BoardResponse<PlayerStatus> getPlayerStatus(int playerIndex);
    public BoardResponse<String> buyCar(int playerIndex, boolean loan, boolean payWithCash);
    public BoardResponse<String> buyHouse(int playerIndex, boolean loan, boolean payWithCash);
    public BoardResponse<String> buyInsurance(int playerIndex, boolean payWithCash, Insurance insurance);
    public BoardResponse<String> buyHouseAsset(int playerIndex, boolean payWithCash, HouseAsset houseAsset);
    public BoardResponse<String> buyBKVPass(int playerIndex, boolean payWithCash);
    public BoardResponse<String> transferMoney(int playerIndex, int amount);

    public List<Field> getTable();
}
