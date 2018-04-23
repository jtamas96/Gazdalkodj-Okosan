package hu.elte.gazdalkodjokosan.persistence;

import hu.elte.gazdalkodjokosan.data.Player;
import hu.elte.gazdalkodjokosan.data.Field;
import hu.elte.gazdalkodjokosan.service.BoardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Persistence implements IPersistence {

    BoardService boardService;

    @Autowired
    public Persistence(BoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public Player getPlayer(int index) {
        return boardService.getPlayer(index);
    }

    @Override
    public List<Field> updateFields() {
        return boardService.getTable();
    }

    @Override
    public void requestNewGame(int playerNumber) {
        boardService.getNewGame(playerNumber);
    }   

}
