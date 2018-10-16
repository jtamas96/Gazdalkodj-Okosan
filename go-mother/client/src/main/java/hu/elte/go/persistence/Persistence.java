package hu.elte.go.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.go.BoardResponse;
import hu.elte.go.data.Field;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.NewGameRequestDTO;
import hu.elte.go.dtos.NewGameStartedDTO;
import hu.elte.go.events.NewGameStartedEvent;
import hu.elte.go.exceptions.PlayerNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Persistence implements IPersistence {

    private final ApplicationEventPublisher publisher;
    private StompSession stompSession;
    Logger logger = Logger.getLogger(Persistence.class.getName());

    @Autowired
    public Persistence(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        try {
            ListenableFuture<StompSession> futureSession = connect();
            stompSession = futureSession.get();
            subscribeToNewGame(stompSession);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ListenableFuture<StompSession> connect() {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/go";
        return stompClient.connect(url, headers, new MyHandler(), "localhost", 8080);
    }

    @Override
    public Player getPlayer(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Field> getFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void requestNewGame(int playerNumber) throws PlayerNumberException {
        this.requestNewGame(stompSession, playerNumber);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player getCurrentPlayer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void requestStep() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player switchPlayer(int currentPlayerIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class MyHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            System.out.println("Now connected");
        }
    }

    private void subscribeToNewGame(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/newGameResponse", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ObjectMapper mapper = new ObjectMapper();
                NewGameStartedDTO newGameDTO;
                try {
                    String json = new String((byte[]) o);
                    newGameDTO = mapper.readValue(json, NewGameStartedDTO.class);
                    NewGameStartedEvent newGameEvent = new NewGameStartedEvent(this, newGameDTO.getTable(), newGameDTO.getPlayers(), newGameDTO.getCurrentPlayer());
                    publisher.publishEvent(newGameEvent);
                } catch (IOException ex) {
                    Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void requestNewGame(StompSession stompSession, int playerNumber) {
        NewGameRequestDTO ngr = new NewGameRequestDTO(playerNumber);
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(ngr);
            stompSession.send("/app/newGame", json.getBytes());
        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /*@Override
    public Player getPlayer(int index) {
        return boardService.getPlayer(index).getValue();
    }

    @Override
    public List<Field> getFields() {
        return boardService.getTable();
    }

    @Override
    public void requestNewGame(int playerNumber) throws PlayerNumberException {
        BoardResponse<List<Field>> response = boardService.getNewGame(playerNumber);

        if (!response.isActionSuccessful()) {
            throw new PlayerNumberException(response.getErrorMessage());
        }
    }

    @Override
    public List<Player> getPlayersOnFiled() {
        return boardService.getPlayersOnFiled().getValue();
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
    public Player switchPlayer(int currentPlayerIndex
    ) {
        BoardResponse<Player> resp = boardService.switchToNextPlayer(currentPlayerIndex);
        if (resp.isActionSuccessful()) {
            return resp.getValue();
        } else {
            System.out.println("Error:" + resp.getErrorMessage());
        }
        return null;
    }

    @Override
    public BoardResponse<List<Item>> buyItems(List<Item> itemsToPurchase
    ) {
        return boardService.buyItems(itemsToPurchase);
    }

    @EventListener
    public void GameStepped(GameSteppedDTO event
    ) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameSteppedDTO(this, event.getCurrentPlayer(), event.getTable()));
        }
    }

    @EventListener
    public void SendMessage(MessageDTO event
    ) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new MessageDTO(this, event.getMessage()));
        }
    }

    @EventListener
    public void UpdatePlayer(UpdatePlayerDTO event
    ) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new UpdatePlayerDTO(this, event.getPlayer()));
        }
    }

    @EventListener
    public void BuyItems(BuyDTO event
    ) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new BuyDTO(this, event.getPlayer(), event.getItemPrices()));
        }
    }

    @EventListener
    public void GameOver(GameOverDTO event
    ) {
        if (event.getSource().equals(boardService)) {
            publisher.publishEvent(new GameOverDTO(this, event.getWinners()));
        }
    }*/
}
