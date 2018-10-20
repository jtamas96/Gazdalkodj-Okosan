package hu.elte.go.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.elte.go.BoardResponse;
import hu.elte.go.data.Player;
import hu.elte.go.data.enums.Item;
import hu.elte.go.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
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
            subscribeTo("/newGameResponse", this, new TypeReference<BoardResponse<NewGameStartedDTO>>() {});
            subscribeTo("/gameStepped", this, new TypeReference<BoardResponse<GameSteppedDTO>>() {});
            subscribeTo("/switchPlayerResponse", this, new TypeReference<BoardResponse<PlayerSwitchedDTO>>() {});
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
    public void requestNewGame(int playerNumber) {
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

    @Override
    public void requestStep() {
        String json = "{}";
        stompSession.send("/app/step", json.getBytes());
    }

    @Override
    public void switchPlayer(int currentPlayerIndex) {
        String json = "" + currentPlayerIndex;
        stompSession.send("/app/switchPlayer", json.getBytes());
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

    private <D extends EventConvertible<E>, E extends ApplicationEvent>
    void subscribeTo(String path, Persistence source, TypeReference responseTypeRef) {
        stompSession.subscribe(path, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ObjectMapper mapper = new ObjectMapper();
                BoardResponse<D> response;
                try {
                    String json = new String((byte[]) o);
                    response = mapper.readValue(json, responseTypeRef);
                    if(response.isActionSuccessful()){
                        D dto = response.getValue();
                        E event = dto.toEvent(source);
                        publisher.publishEvent(event);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
    public List<Player> getPlayersOnFiled() {
        return boardService.getPlayersOnFiled().getValue();
    }

    @Override
    public Player getCurrentPlayer() {
        return boardService.getCurrentPlayer().getValue();
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
