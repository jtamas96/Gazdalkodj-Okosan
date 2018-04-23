package hu.elte.gazdalkodjokosan.model;

import hu.elte.gazdalkodjokosan.persistence.IPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientModel {
    
    IPersistence persistence;
    
    @Autowired
    public ClientModel(IPersistence persistence) {
        this.persistence = persistence;
    }
    
    public void newGame(int playerNumber) {
        persistence.requestNewGame(playerNumber);
    }
    
}
