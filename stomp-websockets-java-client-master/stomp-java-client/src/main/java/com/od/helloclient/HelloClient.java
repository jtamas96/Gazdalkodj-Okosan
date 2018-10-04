package com.od.helloclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * Created by nick on 30/09/2015.
 */
public class HelloClient {

    private static Logger logger = Logger.getLogger(HelloClient.class);

    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/hello";
        return stompClient.connect(url, headers, new MyHandler(), "10.10.18.129", 8080);
    }

    public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/greetings", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Greeting greeting = mapper.readValue(new String((byte[]) o), Greeting.class);
                    logger.info("Received greeting: " + greeting.getContent());
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(HelloClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void sendHello(StompSession stompSession) {
        HelloMessage hello = new HelloMessage("Test");
        ObjectMapper mapper = new ObjectMapper();
        String jsonHello;
        try {
            jsonHello = mapper.writeValueAsString(hello);
            stompSession.send("/app/hello", jsonHello.getBytes());
        } catch (JsonProcessingException ex) {
            java.util.logging.Logger.getLogger(HelloClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class MyHandler extends StompSessionHandlerAdapter {

        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }

    public static void main(String[] args) throws Exception {
        HelloClient helloClient = new HelloClient();

        ListenableFuture<StompSession> f = helloClient.connect();
        StompSession stompSession = f.get();

        logger.info("Subscribing to greeting topic using session " + stompSession);
        helloClient.subscribeGreetings(stompSession);

        logger.info("Sending hello message" + stompSession);
        helloClient.sendHello(stompSession);
        Thread.sleep(60000);
    }

}
