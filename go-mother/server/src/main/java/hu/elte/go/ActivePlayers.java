package hu.elte.go;

import hu.elte.go.data.Player;
import hu.elte.go.data.SaleItem;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActivePlayers {
    private Map<String, Player> playersMap;

    public ActivePlayers() {
        this.playersMap = new HashMap<>();
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
}
