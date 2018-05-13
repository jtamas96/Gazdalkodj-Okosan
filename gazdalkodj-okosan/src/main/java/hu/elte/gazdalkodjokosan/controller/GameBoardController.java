/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.common.transfer.Insurance;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.BuyEvent;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.view.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class GameBoardController implements Initializable {

    ClientModel clientModel;

    @Autowired @Lazy
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
    GameBoardController(ClientModel clientModel) {
        this.clientModel = clientModel;
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
        displayPlayerInfo();
    }

    private void displayPlayerInfo() {
        Player currentPlayer = clientModel.getCurrentPlayer();
        playerColor.setText("Játékos " + clientModel.getCurrentPlayer().getIndex());
        balance.setText(currentPlayer.getBankBalance() + "");
        fieldIndex.setText(currentPlayer.getPosition() + "");
        setWinningCriteriaIndicators(currentPlayer);
    }

    @FXML
    public void endRoundAction(ActionEvent event) {
        System.out.println("kör vége");
        clientModel.switchPlayer();
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
            // TODO
        }
    }

    @EventListener
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(clientModel)) {
            //Todo react, refresh the view!
        }
    }

    public void stepGame(javafx.event.ActionEvent actionEvent) {
        clientModel.stepGame();
        displayPlayerInfo();
    }

    @FXML
    public void turnOverRequest(javafx.event.ActionEvent actionEvent) {
        clientModel.switchPlayer();
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(clientModel)) {
            //Todo react
            Notifications notiBuilder = Notifications.create()
                    .title("Gazdalkodj okosan!")
                    .text(event.getMessage())
                    .graphic(null)
                    .hideAfter(Duration.seconds(7))
                    .position(Pos.BASELINE_RIGHT);
            notiBuilder.showInformation();

        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerEvent event) {
        if (event.getSource().equals(clientModel)) {
            //Todo react
        }
    }

    @EventListener
    public void BuyItems(BuyEvent event) {
        if (event.getSource().equals(clientModel)) {
            System.out.println("Vasarolsz most !");
        }
    }
}
