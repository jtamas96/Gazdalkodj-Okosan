/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.events.ConnectToServer;
import hu.elte.go.events.NewGameStartedEvent;
import hu.elte.go.model.ClientModel;
import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
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
 * @author sando
 */
@Controller
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
        RadioButton selectedRadioButton = (RadioButton) playerNum.getSelectedToggle();
        String value = selectedRadioButton.getText();

        clientModel.newGame(Integer.parseInt(value));
    }

    @EventListener
    public void connectedToServer(ConnectToServer event) {
        Platform.runLater(() -> {
            if(event.isSuccessful()){
                stageManager.switchScene(FxmlView.USERNAME);
            } else {
                Notifications notification = Notifications.create()
                        .title(getStringFromResourceBundle("window.connect.error.title"))
                        .text(getStringFromResourceBundle("window.connect.error.text"))
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.CENTER);
                notification.showError();
            }
        });
    }

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
