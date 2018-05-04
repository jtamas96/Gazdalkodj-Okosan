/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.model.ClientModel;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class StarterController implements Initializable {

    // BoardService boardService;
    ClientModel clientModel;
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
        // TODO
    }

    @FXML
    private void startGamePressed(ActionEvent event) throws IOException {
        try {
            clientModel.newGame(3);
        } catch (PlayerNumberException ex) {
           //Todo error message
        }
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/GameBoard.fxml"));
        Scene scene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.centerOnScreen();
        window.show();
    }

}
