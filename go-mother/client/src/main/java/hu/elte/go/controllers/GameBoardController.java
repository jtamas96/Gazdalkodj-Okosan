/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.BoardResponse;
import hu.elte.go.PlayerColor;
import hu.elte.go.controllers.consts.PawnPosition;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.events.*;
import hu.elte.go.model.ClientModel;
import hu.elte.go.view.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    Label balance;
    @FXML
    ImageView pawn1;
    @FXML
    ImageView pawn2;
    @FXML
    ImageView pawn3;
    @FXML
    ImageView pawn4;
    @FXML
    ImageView pawn5;
    @FXML
    ImageView pawn6;
    @FXML
    Label message;

    @FXML
    ListView<Map.Entry<String, Integer>> shoppingList;

    @FXML
    ListView<String> purchasedList;

    @FXML
    ImageView playerIndicator;

    List<ImageView> pawnList;

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set messageLabel
        message.setWrapText(true);
        // set shoppingList
        shoppingList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // map players and colors
        pawnList = new ArrayList<ImageView>(Arrays.asList(pawn1, pawn2, pawn3, pawn4, pawn5, pawn6));
        pawnList.forEach(pawn -> pawn.setVisible(false));
        clientModel.getPlayers().forEach(player -> {
            // get pawn for player
            ImageView pawn = pawnList.get(player.getIndex());
            // set color
            pawn.setImage(new Image("/images/board_pawn_"
                    + (PlayerColor.values()[player.getIndex()]).toString().toLowerCase() + ".png")
            );
            // set visible
            pawn.setVisible(true);
            movePawn(player);
        });

        // debug
        System.out.println("Játékosok száma: " + clientModel.getPlayers().size());
        System.out.println("Játékosok: " + Arrays.toString(clientModel.getPlayers().stream().map(
                p -> "Index: " + p.getIndex()).toArray()));
        System.out.println("Soronlévő játékos: " + clientModel.getCurrentPlayer().getIndex());
        // display info for current player
        displayPlayerInfo(clientModel.getCurrentPlayer());
    }

    private void displayPlayerInfo(Player currentPlayer) {
        movePawn(currentPlayer);
        balance.setText(currentPlayer.getBankBalance() + "");
        populatePurchasedList(currentPlayer);
        setPlayerIndicator(currentPlayer);
    }

    private void setPlayerIndicator(Player currentPlayer) {
        playerIndicator.setImage(new Image("/images/indicator_pawn_"
                + (PlayerColor.values()[currentPlayer.getIndex()]).toString().toLowerCase() + ".png")
        );
    }

    private void movePawn(Player currentPlayer) {
        int index = currentPlayer.getIndex();
        ImageView playerPawn = pawnList.get(index);
        int position = currentPlayer.getPosition();
        playerPawn.setLayoutX(PawnPosition.calcX(position, index));
        playerPawn.setLayoutY(PawnPosition.calcY(position, index));
    }

    private void populatePurchasedList(Player currentPlayer) {
        ObservableList<String> purchasedItems = FXCollections.observableArrayList(
                currentPlayer.getItems().stream()
                        .filter(i -> i.isPurchased())
                        .map(i -> i.getName()).collect(Collectors.toList()
                )
        );
        purchasedList.setItems(purchasedItems);
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
        if (!clientModel.isGameOver()) {
            displayPlayerInfo(clientModel.getCurrentPlayer());
            shoppingList.setItems(null);
            message.setText("");
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(clientModel)) {
            message.setText(event.getMessage());
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
            System.out.println(Arrays.toString(event.getItemPrices().keySet().toArray()));
            System.out.println("valuek:" + Arrays.toString(event.getItemPrices().values().toArray()));
            Map<String, Integer> purchaseableItems = event.getItemPrices();
            ObservableList<Map.Entry<String, Integer>> shoppingListItems = FXCollections.observableArrayList(purchaseableItems.entrySet());
            shoppingList.setItems(shoppingListItems);
            buyItems.setDisable(false);
        }
    }

    @FXML
    public void buyItemButton(javafx.event.ActionEvent actionEvent) {
        ObservableList<Map.Entry<String, Integer>> selectedItems = shoppingList.getSelectionModel().getSelectedItems();
        if (selectedItems.size() == 0) {
            return;
        }

        System.out.println("User selected: ");
        selectedItems.forEach(System.out::println);
        BoardResponse<List<Item>> listBoardResponse = clientModel.buyItems(
                selectedItems.stream()
                        .map(Map.Entry::getKey)
                        .map(Item::valueOf)
                        .collect(Collectors.toList())
        );
        if (listBoardResponse.isActionSuccessful()) {
            shoppingList.getItems().removeAll(selectedItems);
            if (shoppingList.getItems().isEmpty()) {
                buyItems.setDisable(true);
            }
            populatePurchasedList(clientModel.getCurrentPlayer());
            message.setText("Sikeres vásárlás");
        } else {
            message.setText("Túl sok mindent szedtél össze.");
        }
    }

    @EventListener
    public void GameOver(GameOverEvent event) {
        if (event.getSource().equals(clientModel)) {
            Integer[] ind = Stream.of(event.getWinners()).map(p -> p.getIndex()).toArray(Integer[]::new);
            message.setText("Vége a játéknak. Győztesek: " + Arrays.toString(ind));
        }
    }
}
