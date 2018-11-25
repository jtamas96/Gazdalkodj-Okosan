/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.go.controllers;

import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.events.ErrorEvent;
import hu.elte.go.events.JoinedToRoomEvent;
import hu.elte.go.events.NewGameStartedEvent;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    public Button startGame;
    @FXML
    public CheckBox joinCheck;
    @FXML
    public ListView playersOfRoom;
    @FXML
    private ListView roomList;
    @FXML
    private Button createRoom;

    private RoomsModel roomsModel;

    @Autowired
    @Lazy
    StageManager stageManager;
    private List<String> roomUids;

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
        playersOfRoom.setEditable(false);
        playersOfRoom.setMouseTransparent( true );
        playersOfRoom.setFocusTraversable( false );
        roomsModel.refreshRooms();
        joinCheck.setAllowIndeterminate(false);
        // TODO
    }

    private void updateRoomList(){
        ObservableList<String> roomNames = FXCollections.observableArrayList(
                roomsModel.getRooms().stream()
                        .map(r -> {
                            String uuidPart = r.uuid.substring(0, Math.min(6, r.uuid.length()));
                            return r.name + " (" + uuidPart+ ")";
                        })
                        .collect(Collectors.toList()
                )
        );
        roomList.setItems(roomNames);
        roomUids = roomsModel.getRooms()
                .stream().map(r -> r.uuid)
                .collect(Collectors.toList());
    }

    @EventListener
    public void roomResponse(RoomsRefreshEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> {
                updateRoomList();
            });
        }
    }

    @EventListener
    public void joined(JoinedToRoomEvent event){
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> {
                Notifications notification = Notifications.create()
                        .title(ResourceBundle.getBundle("Bundle").getString(("window.room.list.title")))
                        .text(ResourceBundle.getBundle("Bundle").getString(("window.room.join.success.text")))
                        .hideAfter(Duration.seconds(1))
                        .position(Pos.CENTER);
                notification.showConfirm();
            });
        }
    }
    @EventListener
    public void newGameStarted(NewGameStartedEvent event) {
        if(event.getSource().equals(roomsModel)){
            Platform.runLater(() -> stageManager.switchScene(FxmlView.BOARD));
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

    public void joinChanged(ActionEvent actionEvent) {
        if(roomsModel.isJoinedToRoom() || !joinCheck.isSelected()){
            joinCheck.setSelected(true);
            System.out.println("Already joined to a room.");
            return;
        }
        Optional<String> roomUidOpt = getSelectedRoomUuid();
        roomUidOpt.ifPresent(uid -> roomsModel.joinRoom(uid));
    }

    public void updatePlayersList(MouseEvent mouseEvent) {
        Optional<String> roomUidOpt = getSelectedRoomUuid();
        roomUidOpt.ifPresent(uid -> {
            Optional<RoomDetailsDTO> roomOpt = roomsModel.getRoom(uid);
            roomOpt.ifPresent(room -> {
                ObservableList<String> playerNames = FXCollections.observableArrayList(room.players);
                playersOfRoom.setItems(playerNames);
            });
        });
    }

    private Optional<String> getSelectedRoomUuid() {
        ObservableList selected = roomList.getSelectionModel().getSelectedIndices();
        if (selected.size() != 1) {
            System.out.println("Ambiguous room selection.");
            return Optional.empty();
        }
        int index = (int) selected.get(0);
        String roomUid = roomUids.get(index);
        return Optional.of(roomUid);
    }

    public void startGameFunction(ActionEvent actionEvent) {
        roomsModel.startGame();
    }
}
