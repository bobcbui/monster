package net.ttcxy.chat.contorller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import net.ttcxy.chat.code.JwtFilter;
import net.ttcxy.chat.entity.model.CtsMember;

@ServerEndpoint(value = "/api/ws/{token}")
@Component
public class SocketController {

    private static Map<String,Session> tokenSessionMap = new HashMap<>();
    
    private static Map<String,CtsMember> sessionIdMember = new HashMap<>();

    private static Map<String,List<Session>> subscribeMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {

        CtsMember ctsMember = JwtFilter.memberMap.get(token);
        
        if(ctsMember == null){
            JSONObject object = new JSONObject();
            object.put("cmd", "to_login");
            session.getAsyncRemote().sendText(object.toJSONString());
            // 关闭连接
            try {
                session.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        tokenSessionMap.put(token, session);
        sessionIdMember.put(session.getId(), JwtFilter.memberMap.get(token));
        System.out.println("连接成功："+token);
    }
    @OnClose
    public void onClose() {
       
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject request = JSONObject.parseObject(message);
        String subscribeId = request.getString("subscribeId");
        List<Session> list = subscribeMap.get(subscribeId);

        JSONObject response = new JSONObject();
        response.put("html", request.getString("html"));
        response.put("url", request.getString("url"));
        response.put("time", new Date().getTime());
        response.put("subscribeId", subscribeId);
        if(list != null){
            for (Session toSession : list) {
                toSession.getAsyncRemote().sendText(response.toJSONString());
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
  
}
