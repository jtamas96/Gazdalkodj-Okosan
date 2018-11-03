package hu.elte.go.controller;

import hu.elte.go.ActivePlayers;
import hu.elte.go.BoardResponse;
import hu.elte.go.data.Player;
import hu.elte.go.dtos.PlayerCreationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {
    // Currently we support one player per client.
    private ActivePlayers playersMapping;

    @Autowired
    public PlayerController(ActivePlayers playersMapping) {
        this.playersMapping = playersMapping;
    }

    @MessageMapping("/createPlayer/{uuid}/{name}")
    @SendTo("/createPlayerResponse/{uuid}")
    public BoardResponse<PlayerCreationDTO> createPlayer(@DestinationVariable String uuid, @DestinationVariable String name) {
        System.out.println("Player creation request with id: " + uuid + " and name :" + name);
        BoardResponse<PlayerCreationDTO> response;
        Player p = playersMapping.getPlayer(uuid);
        if (p != null) {
            response = new BoardResponse<>("Player UUID already exists.", false, null);
        } else {
            playersMapping.createPlayer(uuid, name);
            PlayerCreationDTO dto = new PlayerCreationDTO(name, uuid);
            response = new BoardResponse<PlayerCreationDTO>("", true, dto);
        }
        return response;
    }
}
