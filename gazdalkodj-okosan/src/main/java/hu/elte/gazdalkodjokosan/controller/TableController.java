package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.model.Room;
import hu.elte.gazdalkodjokosan.service.HardcodedListRoomService;
import hu.elte.gazdalkodjokosan.service.RoomService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.stereotype.Component;

@Component
public class TableController {
    @FXML
    public ComboBox<Room> roomsComboBox;
    private RoomService roomService;

    @FXML
    public void initialize() {
        roomService = new HardcodedListRoomService();
        roomsComboBox.setConverter(Room.getStringConverter());
        roomsComboBox.setItems(FXCollections.observableArrayList(roomService.getAvailableRooms()));
    }


}
