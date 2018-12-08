/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.common.transfer.PlayerColor;
import hu.elte.gazdalkodjokosan.controller.consts.PawnPosition;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.enums.Item;
import hu.elte.gazdalkodjokosan.events.BuyEvent;
import hu.elte.gazdalkodjokosan.events.GameOverEvent;
import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.view.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

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
        stageManager.getScene().getAccelerators().put(
            new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_ANY),
            () -> {
                TextInputDialog dialog = new TextInputDialog("mezőszám..");
                dialog.setTitle("Tetszőleges mező");
                dialog.setHeaderText("Cheat");
                dialog.setContentText("Add meg a kívánt mező sorszámát!");
                Optional<String> fieldNum = dialog.showAndWait();
                while (fieldNum.isPresent() && convertToFieldNum(fieldNum.get()) == -1) {
                    dialog = new TextInputDialog("új mezőszám..");
                    dialog.setTitle("Tetszőleges mező");
                    dialog.setHeaderText("Cheat");
                    dialog.setContentText("Nem megfelelő mezőszám. Add meg a kívánt mező sorszámát!");
                    fieldNum = dialog.showAndWait();
                }
                cheatStep(convertToFieldNum(fieldNum.get()));
            }
        );
    }

    /*
    returns -1 if position is invalid
    */
    private int convertToFieldNum(String fieldNum) {
        int intFieldNum = -1;
        // validate if convertible to int
        try {
            intFieldNum = Integer.parseInt(fieldNum);
        } catch (Exception e) {
            return -1;
        }
        // validate if valid number
        if (intFieldNum < 1 || intFieldNum > 42 )
            return -1;
        
        return intFieldNum;
    }
    
    private void cheatStep(int fieldNum) {
        System.out.println("Cheat: a " + fieldNum + ". számú mezőre lépünk");
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
    public void handleCheat(KeyEvent event) {
        System.out.println("event: " + event.getCode());
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
