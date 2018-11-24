/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.events.ErrorEvent;
import hu.elte.go.events.PlayerCreatedEvent;
import hu.elte.go.model.ClientModel;
import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Sandor
 */
@Controller
public class NamePromptController implements Initializable, ErrorHandlerBase {

    @FXML
    private TextField userName;

    public ClientModel clientModel;
    @Autowired
    @Lazy
    StageManager stageManager;

    @Autowired
    NamePromptController(ClientModel clientModel){
        this.clientModel = clientModel;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void OKPressed(ActionEvent event) throws IOException {
        clientModel.createPlayer(userName.getText());
    }

    @EventListener
    public void playerCreated(PlayerCreatedEvent event) {
        if(event.getSource().equals(clientModel)){
            Platform.runLater(() -> {
                stageManager.switchScene(FxmlView.ROOMS);
            });
        }
    }

    @EventListener
    public void errorHandler(ErrorEvent event){
        if(event.getSource().equals(clientModel)){
            Platform.runLater(() -> handleError(event));
        }
    }
}
