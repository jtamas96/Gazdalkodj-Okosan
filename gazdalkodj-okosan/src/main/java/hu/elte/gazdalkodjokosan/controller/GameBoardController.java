/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.common.transfer.Insurance;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.view.StageManager;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class GameBoardController implements Initializable {

    ClientModel clientModel;
    StageManager stageManager;
    
    @FXML
    Label playerColor;
    @FXML
    Label balance;    
    @FXML
    Label fieldIndex;
    
    @FXML
    CheckBox housePurchased;
    @FXML
    CheckBox carPurchased;
    @FXML
    CheckBox carInsurancePurchased;
    @FXML
    CheckBox kitchenFurniturePurchased;
    @FXML
    CheckBox roomFurniturePurchased;
    @FXML
    CheckBox washingMachinePurchased;
    @FXML
    CheckBox fridgePurchased;
    @FXML
    CheckBox stovePurchased;
    @FXML
    CheckBox tvPurchased;
    
    
    @Autowired
    @Lazy
    GameBoardController(ClientModel clientModel, StageManager stageManager) {
        this.clientModel = clientModel;
        this.stageManager = stageManager;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Játékosok száma: " + clientModel.getPlayers().size());
        System.out.println("Játékosok: " + Arrays.toString(clientModel.getPlayers().stream().map(
            p -> "Index: " + p.getIndex()).toArray()));
        System.out.println("Soronlévő játékos: " + clientModel.getCurrentPlayer().getIndex());
        
        
        startRound();
    }
    
    private void startRound() {
        clientModel.stepGame();
        displayPlayerInfo();
    }
    
    private void displayPlayerInfo() {
        Player currentPlayer = clientModel.getCurrentPlayer();
        playerColor.setText("Játékos " + clientModel.getCurrentPlayer().getIndex());
        balance.setText(currentPlayer.getBankBalance()+"");
        fieldIndex.setText(currentPlayer.getPosition()+"");
        setWinningCriteriaIndicators(currentPlayer);
    }
    
    @FXML
    public void endRoundAction(ActionEvent event){
        System.out.println("kör vége");
        clientModel.endRound();
        startRound();
    }
    
    private void setWinningCriteriaIndicators(Player player) {
        indicateOwnership(housePurchased, player.isWithHouse());
        indicateOwnership(carPurchased, player.isWithCar());
        indicateOwnership(carInsurancePurchased, player.getInsurances().contains(Insurance.CAR));
        indicateOwnership(kitchenFurniturePurchased, player.getItems().contains(Item.KONYHA_BUTOR));
        indicateOwnership(roomFurniturePurchased, player.getItems().contains(Item.KONYHA_BUTOR));
        indicateOwnership(washingMachinePurchased, true);
        indicateOwnership(fridgePurchased, true);
        indicateOwnership(stovePurchased, true);
        indicateOwnership(tvPurchased, player.getItems().contains(Item.TV));
    }
    
    private void indicateOwnership(CheckBox checkBox, boolean value) {
        
        if (value) {
            checkBox.setStyle("-fx-opacity: 1");
            checkBox.setSelected(true);
        } else {
            checkBox.setDisable(true);
            //checkBox.setSelected(false);
        }
    }
}
