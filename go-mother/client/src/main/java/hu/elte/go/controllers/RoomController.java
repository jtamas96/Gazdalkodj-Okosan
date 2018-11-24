/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.events.ErrorEvent;
import hu.elte.go.events.RoomsRefreshEvent;
import hu.elte.go.model.RoomsModel;
import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author Sandor
 */
@Controller
public class RoomController implements Initializable, ErrorHandlerBase {

    @FXML
    private ListView roomList;
    @FXML
    private Button createRoom;

    private RoomsModel roomsModel;

    @Autowired
    @Lazy
    StageManager stageManager;

    @Autowired
    public RoomController(RoomsModel roomsModel) {
        this.roomsModel = roomsModel;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomsModel.refreshRooms();
        // TODO
    }

    private void updateRoomList(){
        ObservableList<String> purchasedItems = FXCollections.observableArrayList(
                roomsModel.getRooms().stream()
                        .map(r -> r.name + " (" + r.uuid + ")")
                        .collect(Collectors.toList()
                )
        );
        roomList.setItems(purchasedItems);
    }

    @EventListener
    public void roomResponse(RoomsRefreshEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> {
                updateRoomList();
            });
        }
    }

    public void createRoomPressed(ActionEvent actionEvent){
        stageManager.switchScene(FxmlView.ROOMCREATE);
    }


    @EventListener
    public void errorHandler(ErrorEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> handleError(event));
        }
    }

}
