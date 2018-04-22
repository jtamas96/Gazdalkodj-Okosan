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
public interface BoardService {
    
    // initialise game, return the first player's color in the row
    public PlayerColor getNewGame(int nrPlayers);
    // return the next player's color in the row
    public PlayerColor switchToNextPlayer(PlayerColor playerColor);
    // move the player from its current place #nrSteps forward
    public BoardResponse move(PlayerColor playerColor, int nrSteps);
    public PlayerStatus getPlayerStatus(PlayerColor playerColor);
    public BoardResponse buyCar(PlayerColor playerColor, boolean loan, boolean payWithCash);
    public BoardResponse buyHouse(PlayerColor playerColor, boolean loan, boolean payWithCash);
    public BoardResponse buyInsurance(PlayerColor playerColor, boolean payWithCash, Insurance insurance);
    public BoardResponse buyHouseAsset(PlayerColor playerColor, boolean payWithCash, HouseAsset houseAsset);
    public BoardResponse buyBKVPass(PlayerColor playerColor, boolean payWithCash);
    public BoardResponse transferMoney(PlayerColor playerColor, int amount);
}
