package net.ttcxy.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/message/{id}")
@Component
public class SocketMessage {

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String memberId) {
        session.getPathParameters().put("memberId", memberId);
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String url = session.getPathParameters().get("url");
        String token = session.getPathParameters().get("token");
        String memberId = session.getPathParameters().get("memberId");
        JSONObject obj = JSONObject.parseObject(message);
        
        // 加入
        if("join".equals(obj.getString("type"))){

        }
        
        // 添加为好友
        if("add".equals(obj.getString("type"))){
            
        }

        // 删除好友
        if("delete".equals(obj.getString("type"))){
            
        }

        // 发送消息
        if("delete".equals(obj.getString("type"))){
            
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
