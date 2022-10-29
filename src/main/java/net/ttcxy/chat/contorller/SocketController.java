package net.ttcxy.chat.contorller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/socket/{token}")
@Component
public class SocketController {

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        System.out.println("连接成功："+token);
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
