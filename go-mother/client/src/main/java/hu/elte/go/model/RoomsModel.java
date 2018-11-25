package hu.elte.go.model;

import hu.elte.go.dtos.RoomDetailsDTO;
import hu.elte.go.events.JoinedToRoomEvent;
import hu.elte.go.events.RoomCreatedEvent;
import hu.elte.go.events.RoomsRefreshEvent;
import hu.elte.go.persistence.IPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RoomsModel {

    private List<RoomDetailsDTO> rooms;
    private String roomOfPlayer;

    private boolean joinedToRoom;

    private final ApplicationEventPublisher publisher;

    private final IPersistence persistence;
    @Autowired
    public RoomsModel(ApplicationEventPublisher publisher, IPersistence persistence) {
        this.publisher = publisher;
        this.persistence = persistence;
        rooms = new ArrayList<>();
        roomOfPlayer = null;
        joinedToRoom = false;
    }

    public void refreshRooms(){
        persistence.getRoomList();
    }

    public Optional<RoomDetailsDTO> getRoom(String uuid){
        return  rooms.stream()
                .filter(roomDetailsDTO -> roomDetailsDTO.uuid.equals(uuid))
                .findFirst();
    }

    public List<RoomDetailsDTO> getRooms() {
        return rooms;
    }

    public void createRoom(String roomNameText) {
        persistence.createRoom(roomNameText);
    }

    public boolean isJoinedToRoom() {
        return joinedToRoom;
    }

    public void joinRoom(String roomUuid) {
        roomOfPlayer = roomUuid;
        persistence.joinRoom(roomUuid);
    }

    @EventListener
    public void roomResponse(RoomsRefreshEvent event){
        if(event.getSource().equals(persistence)){
            rooms = event.getRooms();
            publisher.publishEvent(new RoomsRefreshEvent(this, rooms));
        }
    }

    @EventListener
    public void roomCreated(RoomCreatedEvent event){
        if(event.getSource().equals(persistence)){
            roomOfPlayer = event.getUuid();
            joinedToRoom = true;
            publisher.publishEvent(new RoomCreatedEvent(this, event));
        }
    }

    @EventListener
    public void joined(JoinedToRoomEvent event){
        if(event.getSource().equals(persistence)){
            joinedToRoom = true;
            publisher.publishEvent(new JoinedToRoomEvent(this));
        }
    }
}
