package unit;

import hu.elte.go.data.Player;
import hu.elte.go.data.SaleItem;
import hu.elte.go.data.enums.Item;
import hu.elte.go.events.BuyEvent;
import hu.elte.go.events.GameSteppedEvent;
import hu.elte.go.events.MessageEvent;
import hu.elte.go.events.PlayerUpdateEvent;
import hu.elte.go.exceptions.BuyException;
import hu.elte.go.model.GameModel;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import org.springframework.context.ApplicationEventPublisher;

public class GameModelUnitTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private GameModel model;

    @Before
    public void init() {
        initMocks(this);
        model = new GameModel(eventPublisher, "1");
    }

    @Test
    public void NeGametest() {
        assertNull(model.getTable());
        assertNull(model.getPlayers());
        assertNull(model.getDeck());
        List<Player> players = new ArrayList<>();
        players.add(new Player(3000000, "p1", SaleItem.getInitialListForUser()));
        players.add(new Player(3000000, "p2", SaleItem.getInitialListForUser()));
        model.newGame(players);
        assertEquals(model.getTable().size(), 42);
        assertEquals(model.getPlayers().size(), 2);
        assertNotNull(model.getDeck());
        assertEquals(model.getTable().get(0).getPlayersOnFiled().size(), 2);
        for (int i = 1; i < 42; i++) {
            assertEquals(model.getTable().get(i).getPlayersOnFiled().size(), 0);
        }
    }

    @Test
    public void StepGameTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(3000000, "p1", SaleItem.getInitialListForUser()));
        players.add(new Player(3000000, "p2", SaleItem.getInitialListForUser()));
        model.newGame(players);
        model.stepGame();

        verify(eventPublisher, atMost(1)).publishEvent(any(PlayerUpdateEvent.class));
        verify(eventPublisher, atMost(1)).publishEvent(any(BuyEvent.class));
        verify(eventPublisher).publishEvent(any(GameSteppedEvent.class));
        model.getLastStepped().equals(model.getCurrentPlayer());
    }

    @Test
    public void BuyItemTest() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(3000000, "p1", SaleItem.getInitialListForUser()));
        players.add(new Player(3000000, "p2", SaleItem.getInitialListForUser()));
        model.newGame(players);
        model.stepForward(2);
        verify(eventPublisher).publishEvent(any(MessageEvent.class));
        verify(eventPublisher).publishEvent(any(BuyEvent.class));
        verify(eventPublisher).publishEvent(any(GameSteppedEvent.class));
        List<Item> itemList = new ArrayList<>();
        itemList.add(Item.BKV_BERLET);
        try {
            int money = model.getCurrentPlayer().getBankBalance();
            model.buyItems(itemList);
            assertEquals(money - Item.BKV_BERLET.getCost(), model.getCurrentPlayer().getBankBalance());
            assertTrue(model.getCurrentPlayer().getItem(Item.BKV_BERLET).get().isPurchased());
        } catch (BuyException ex) {
            fail();
        }
    }

}
