package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.common.transfer.BoardResponse;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.model.exceptions.PlayerNumberException;
import hu.elte.gazdalkodjokosan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import org.springframework.context.ApplicationEventPublisher;

@Component
public class Persistence implements IPersistence {

    private final BoardService boardService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public Persistence(BoardService boardService, ApplicationEventPublisher publisher) {
        this.boardService = boardService;
        this.publisher = publisher;
    }

    @Override
    public Player getPlayer(int index) {
        return boardService.getPlayer(index).getValue();
    }

    @Override
    public List<Field> getFields() {
        return boardService.getTable();
    }

    @Override
    public List<Field> requestNewGame(int playerNumber) throws PlayerNumberException {
        BoardResponse<List<Field>> response = boardService.getNewGame(playerNumber);

        if (!response.isActionSuccessful()) {
            throw new PlayerNumberException(response.getErrorMessage());
        }
        
        return response.getValue();
    }

    @Override
    public List<Player> getPlayers() {
        return boardService.getPlayers().getValue();
    }

    @Override
    public Player getCurrentPlayer() {
        return boardService.getCurrentPlayer().getValue();
    }

    @Override
    public void requestStep() {
        boardService.stepGame();
    }
    
    @Override
    public void endRound(int playerNumber) {
        boardService.switchToNextPlayer(playerNumber);
    }

}
