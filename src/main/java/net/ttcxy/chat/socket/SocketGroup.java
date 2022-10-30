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
import net.ttcxy.chat.entity.model.CtsMessageGroup;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.CtsMessageGroupRepository;
import net.ttcxy.chat.repository.CtsRelationGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@ServerEndpoint(value = "/socket/group")
@Component
public class SocketGroup {

    @Autowired
    CtsRelationGroupRepository relationGroupRepository;

    @Autowired
    CtsMessageGroupRepository messageGroupRepository;

    Map<String, List<Session>> groupSessionList = new HashMap<>();

    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        String token = session.getPathParameters().get("token");
        String memberUrl = session.getPathParameters().get("memberUrl");
        String groupUrl =  session.getPathParameters().get("groupUrl");
        String body = HttpUtil.get(memberUrl + "?memberUrl=" + memberUrl);
        if (!"true".equals(body)){
            session.getAsyncRemote().sendText("token 无效");
            session.close();
            return;
        }

        CtsRelationGroup groupUrlAndMemberUrl = relationGroupRepository.findByGroupUrlAndMemberUrl(groupUrl, memberUrl);
        if (groupUrlAndMemberUrl == null){
            session.getAsyncRemote().sendText("你无法建立连接");
            session.close();
            return;
        }

        putSession(groupUrl,session);
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
        String toUrl = object.getString("toUrl");

        Date date = new Date();

        JSONObject request = new JSONObject();
        request.put("toUrl",toUrl);
        request.put("text",text);
        request.put("time",date.getTime());
        request.put("fromUrl",session.getPathParameters().get("memberUrl"));

        String groupUrl =  session.getPathParameters().get("groupUrl");
        for (Session groupSession : groupSessionList.get(groupUrl)) {
            groupSession.getAsyncRemote().sendText(request.toJSONString());
        }

        CtsMessageGroup messageGroup = new CtsMessageGroup();
        messageGroup.setGroupUrl(groupUrl);
        messageGroup.setMemberUrl(session.getPathParameters().get("memberUrl"));
        messageGroup.setText(text);
        messageGroup.setCreateTime(date);
        messageGroupRepository.save(messageGroup);

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    private void putSession(String groupUrl,Session session){
        List<Session> sessionList = groupSessionList.get(groupUrl);
        if (sessionList == null){
            sessionList = new ArrayList<>();
        }
        sessionList.add(session);
    }
}
