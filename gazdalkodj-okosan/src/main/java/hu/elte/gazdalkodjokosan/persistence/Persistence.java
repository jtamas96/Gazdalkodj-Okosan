package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.common.transfer.PlayerStatus;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Persistence implements IPersistence {

    BoardService boardService;

    @Autowired
    public Persistence(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public PlayerStatus getPlayer(int index) {
        return boardService.getPlayerStatus(index).getValue();
    }

    @Override
    public List<Field> getFields() {
        return boardService.getTable();
    }

    @Override
    public void requestNewGame(int playerNumber) {
        boardService.getNewGame(playerNumber);
    }   

}
