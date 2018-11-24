/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.events.ErrorEvent;
import hu.elte.go.events.RoomCreatedEvent;
import hu.elte.go.model.RoomsModel;
import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Sandor
 */
@Controller
public class RoomInitController implements Initializable, ErrorHandlerBase {

    public TextField roomName;
    private RoomsModel roomsModel;

    @Autowired
    @Lazy
    StageManager stageManager;

    @Autowired
    public RoomInitController(RoomsModel roomsModel) {
        this.roomsModel = roomsModel;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void onCreate(ActionEvent actionEvent) {
        roomsModel.createRoom(roomName.getText());
    }

    @EventListener
    public void roomCreated(RoomCreatedEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> stageManager.switchScene(FxmlView.ROOMS));
        }
    }

    @EventListener
    public void errorHandler(ErrorEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> handleError(event));
        }
    }
}
