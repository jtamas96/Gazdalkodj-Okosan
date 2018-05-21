/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.GazdalkodjOkosanApplication;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.view.FxmlView;
import hu.elte.gazdalkodjokosan.view.StageManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class StarterController implements Initializable {
    Parent parent;

    public ClientModel clientModel;
    @Autowired @Lazy
    StageManager stageManager;


    @FXML
    private ToggleGroup playerNum;

    @Autowired
    StarterController(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void startGamePressed(ActionEvent event) throws IOException {
        try {
            RadioButton selectedRadioButton = (RadioButton) playerNum.getSelectedToggle();
            String value = selectedRadioButton.getText();

            clientModel.newGame(Integer.parseInt(value));
            stageManager.switchScene(FxmlView.BOARD);
        } catch (PlayerNumberException ex) {
           //Now the client UI protects from invalid values!
            //TODO: Error handling in other contexts.
        }
    }

}
