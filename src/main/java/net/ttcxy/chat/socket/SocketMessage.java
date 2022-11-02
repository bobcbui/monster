package net.ttcxy.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/socket/message/{id}")
@Component
public class SocketMessage {

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String memberId) {
        String url = session.getPathParameters().get("url");
        String token = session.getPathParameters().get("token");
         //TODO 1、URL + TOKEN 验证TOKEN
        //TODO 2、判断用户是否存在，不存在存储用户信息
        session.getAsyncRemote().sendText("token");
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
