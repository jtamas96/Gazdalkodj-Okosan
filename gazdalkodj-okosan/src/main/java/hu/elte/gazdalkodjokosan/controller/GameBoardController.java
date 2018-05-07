/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.gazdalkodjokosan.controller;

import hu.elte.gazdalkodjokosan.events.GameSteppedEvent;
import hu.elte.gazdalkodjokosan.events.MessageEvent;
import hu.elte.gazdalkodjokosan.events.UpdatePlayerEvent;
import hu.elte.gazdalkodjokosan.model.ClientModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author sando
 */
@Component
public class GameBoardController implements Initializable {

    ClientModel clientModel;

    @Autowired
    public GameBoardController(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @EventListener
    public void GameStepped(GameSteppedEvent event) {
        if (event.getSource().equals(clientModel)) {
            //Todo react
        }
    }

    @EventListener
    public void SendMessage(MessageEvent event) {
        if (event.getSource().equals(clientModel)) {
           //Todo react
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerEvent event) {
        if (event.getSource().equals(clientModel)) {
            //Todo react
        }
    }

}
