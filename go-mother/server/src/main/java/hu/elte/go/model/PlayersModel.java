package hu.elte.go.model;

import hu.elte.go.data.Player;
import hu.elte.go.data.SaleItem;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PlayersModel {
    private ConcurrentMap<String, Player> playersMap;

    @Autowired
    public PlayersModel() {
        this.playersMap = new ConcurrentHashMap<>();
    }

    public Player getPlayer(String uuid) {
        return playersMap.get(uuid);
    }

    public void createPlayer(String playerUuid, String playerName) {
        Player p = new Player(3000000, playerName, SaleItem.getInitialListForUser());
        this.playersMap.put(playerUuid, p);
    }

    public void deletePlayer(String playerUuid) {
        this.playersMap.remove(playerUuid);
    }
    
    public ConcurrentMap<String, Player> getPlayersMap() {
        return playersMap;
    }
}
