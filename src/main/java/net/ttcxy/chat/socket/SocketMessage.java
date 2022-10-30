package net.ttcxy.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/socket/{name}")
@Component
public class SocketMessage {

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) {
        
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
