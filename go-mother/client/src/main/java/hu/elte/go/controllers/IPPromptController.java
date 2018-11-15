/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.events.NewGameStartedEvent;
import hu.elte.go.model.ClientModel;
import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Sandor
 */
@Component
public class IPPromptController implements Initializable {

    
    Parent parent;

    public ClientModel clientModel;
    @Autowired @Lazy
    StageManager stageManager;


    @FXML
    private Button OKButton;
    
    @FXML
    private TextField IPAddress;

    @Autowired
    IPPromptController(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void OKPressed(ActionEvent event) throws IOException {
        clientModel.connect(IPAddress.getText());
        clientModel.newGame(4);
    }

    @EventListener
    public void NewGameStarted(NewGameStartedEvent event) {
        if (event.getSource().equals(clientModel)) {
            stageManager.switchScene(FxmlView.BOARD);
        }
    } 
}
