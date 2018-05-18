/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.common.transfer.Insurance;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.BuyEvent;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.view.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class GameBoardController implements Initializable {

    ClientModel clientModel;

    @Autowired
    @Lazy
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

    @FXML
    ListView<Map.Entry<Item, Integer>> shoppingList;

    @FXML
    public Button buyItems;

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
//        shoppingList.setCellFactory(param -> new ListCell<java.util.Map.Entry<Item, Boolean>>() {
//            @Override
//            protected void updateItem(Entry<Item, Boolean> item, boolean empty) {
//                super.updateItem(item, empty);
//
//                setText(item.getKey().toString());
//                
//            }
//        });

        shoppingList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayPlayerInfo(clientModel.getCurrentPlayer());
    }

    private void displayPlayerInfo(Player currentPlayer) {
        playerColor.setText("Játékos " + clientModel.getCurrentPlayer().getIndex());
        balance.setText(currentPlayer.getBankBalance() + "");
        fieldIndex.setText(currentPlayer.getPosition() + "");
        setWinningCriteriaIndicators(currentPlayer);
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
            System.out.println("Game stepped.. by" + event.getCurrentPlayer().getIndex());
            displayPlayerInfo(event.getCurrentPlayer());
        }
    }

    public void stepGame(javafx.event.ActionEvent actionEvent) {
        clientModel.stepGame();
    }

    @FXML
    public void turnOverRequest(javafx.event.ActionEvent actionEvent) {
        clientModel.switchPlayer();
        displayPlayerInfo(clientModel.getCurrentPlayer());
        shoppingList.setItems(null);
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
                    .position(Pos.TOP_RIGHT);
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
            System.out.println("Vasarolsz most az alábbiak közül!");
            System.out.println(Arrays.toString(event.getPurchaseAble().keySet().toArray()));
            System.out.println("valuek:" + Arrays.toString(event.getPurchaseAble().values().toArray()));
            Map<Item, Boolean> purchaseableItems = event.getPurchaseAble();
            ObservableList<Map.Entry<Item, Integer>> shoppingListItems = FXCollections.observableArrayList(
                    purchaseableItems.keySet()
                            .stream()
                            .collect(Collectors.toMap(Function.identity(), Item::getCost))
                            .entrySet()
            );
            shoppingList.setItems(shoppingListItems);
            buyItems.setDisable(false);
        }
    }

    @FXML
    public void buyItemButton(javafx.event.ActionEvent actionEvent) {
        ObservableList<Map.Entry<Item, Integer>> selectedItems = shoppingList.getSelectionModel().getSelectedItems();
        System.out.println("User selected: ");
        selectedItems.forEach(System.out::println);
        BoardResponse<List<Item>> listBoardResponse = clientModel.buyItems(
                selectedItems.stream()
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList())
        );
        if(listBoardResponse.isActionSuccessful()){
            System.out.println("Sikeres vasarlas");
        }else{
            System.out.println("Tul sok mindent szedtel ossze.");
        }
        shoppingList.setItems(FXCollections.observableArrayList(new HashSet<>()));
        buyItems.setDisable(true);
    }
}
