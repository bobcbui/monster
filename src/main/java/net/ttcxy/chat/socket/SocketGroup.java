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
 * 建立群聊通道
 */
@ServerEndpoint(value = "/socket/group/{id}")
@Component
public class SocketGroup {

    @OnOpen
    public void onOpen(Session session,@PathParam("id")String groupId) {
        String url = session.getPathParameters().get("url");
        String token = session.getPathParameters().get("token");
        //TODO 1、URL + TOKEN 验证TOKEN
        //TODO 2、判断用户是否存在，不存在存储用户信息
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        // cmd == load // 加载最近的消息

        // 发送消息给所有在线的用户

        // 保存消息

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
