package hu.elte.go;

import hu.elte.go.data.Player;
import hu.elte.go.dtos.ClientDTO;
import hu.elte.go.dtos.NewGameRequest;
import hu.elte.go.dtos.NewGameStartedDTO;
import hu.elte.go.model.GameModel;
import hu.elte.go.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class NewGameController {

    private final GameModel gameModel;

    @Autowired
    public NewGameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @MessageMapping("/hello")
    @SendTo("/helloResponse")
    public ClientDTO hello() throws Exception {
        String id = UUID.randomUUID().toString();
        return new ClientDTO(id);
    }


    @MessageMapping("/newGame")
    @SendTo("/newGameResponse")
    public NewGameStartedDTO newGame(NewGameRequest newGameRequest) throws Exception {
        System.out.println("new game ms recieved");
        gameModel.newGame(newGameRequest.getPlayerNumber());
        List<Player> players =  gameModel.getPlayers();
        NewGameStartedDTO response = new NewGameStartedDTO(gameModel.getTable(), players, gameModel.getCurrentPlayer());
        return response;
    }

}