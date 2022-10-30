package net.ttcxy.chat.socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import net.ttcxy.chat.repository.CtsMessageGroupRepository;
import net.ttcxy.chat.repository.CtsRelationGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@ServerEndpoint(value = "/socket")
@Component
public class SocketSend {

    @Autowired
    CtsRelationGroupRepository relationGroupRepository;

    @Autowired
    CtsMessageGroupRepository messageGroupRepository;

    Map<String, List<Session>> recipientUrlSession = new HashMap<>();

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        String token = session.getPathParameters().get("token");
        String memberUrl = session.getPathParameters().get("memberUrl");
        String body = HttpUtil.get(memberUrl + "?memberUrl=" + memberUrl);
        if (!"true".equals(body)){
            session.getAsyncRemote().sendText("token 无效");
            session.close();
            return;
        }
        putSession(memberUrl,session);
        System.out.println("连接成功："+token);
    }

    @OnClose
    public void onClose() {
       
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        JSONObject object = JSONObject.parseObject(message);
        String messageType = object.getString("type");
        String text = object.getString("text");
        String recipientUrl = object.getString("recipientUrl");
        String memberUrl = session.getPathParameters().get("memberUrl");

        Date date = new Date();

        JSONObject request = new JSONObject();
        request.put("toUrl",recipientUrl);
        request.put("text",text);
        request.put("time",date.getTime());
        request.put("fromUrl",memberUrl);


        for (Session recipientSession : recipientUrlSession.get(recipientUrl)) {
            recipientSession.getAsyncRemote().sendText(request.toJSONString());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    private void putSession(String groupUrl,Session session){
        List<Session> sessionList = recipientUrlSession.get(groupUrl);
        if (sessionList == null){
            sessionList = new ArrayList<>();
        }
        sessionList.add(session);
    }
}
