/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import hu.elte.go.model.ClientModel;
import hu.elte.go.view.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Sandor
 */
@Controller
public class NamePromptController implements Initializable {

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
        //TODO: Real functionality
        clientModel.newGame(3);
    }
    
}
