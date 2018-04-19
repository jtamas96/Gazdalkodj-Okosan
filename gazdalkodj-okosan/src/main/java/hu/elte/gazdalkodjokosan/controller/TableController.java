package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.model.GameModel;
import hu.elte.gazdalkodjokosan.model.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.model.Room;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.service.HardcodedListRoomService;
import hu.elte.gazdalkodjokosan.service.RoomService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableController {

    @FXML
    public ComboBox<Room> roomsComboBox;
    private RoomService roomService;

    @Autowired
    GameModel model;

    public void initialize() {
        roomService = new HardcodedListRoomService();
        roomsComboBox.setConverter(Room.getStringConverter());
        roomsComboBox.setItems(FXCollections.observableArrayList(roomService.getAvailableRooms()));
    }

    public void newGame(int playerNumber) {
        try {
            model.newGame(playerNumber);
        } catch (PlayerNumberException ex) {
            //TODO: error message to user
        }
    }

    public void stepGame() {
        model.stepGame();
    }

    @EventListener
    public void gameStepped(GameSteppedEvent event) {
        //TODO: step the game
    }
}
