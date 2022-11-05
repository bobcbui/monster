package net.ttcxy.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.model.CtsMember;

/**
 * 本地用户接收消息使用
 */
@ServerEndpoint(value = "/local/{token}")
@Component
public class SocketLocal {


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        CtsMember member = ApplicationData.tokenMemberMap.get(token);
        //TODO 发送历史消息给SESSIOn
        
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //TODO 好友消息
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
