package net.ttcxy.chat.code;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * created by huanglei on 2020/09/18
 */
@ServerEndpoint(value = "/tang/ws")
@Component
public class ApplicationWebSocket {

    @OnOpen
    public void onOpen(Session session) {

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
