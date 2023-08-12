package com.ooqn.chat.socket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsGroup;
import com.ooqn.chat.repository.GroupRepository;
import com.ooqn.chat.service.GroupSocketService;

import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/group/{groupName}")
@Component
public class GroupSocket {

    private static GroupSocketService groupSocketService;

    public static Map<String, List<Session>> groupSession = new HashMap<>();

    private static GroupRepository groupRepository;

    @Autowired
    public void setGroupSocketService(GroupSocketService groupSocketService) {
        GroupSocket.groupSocketService = groupSocketService;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        GroupSocket.groupRepository = groupRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().stream().findFirst().orElseThrow());
        }
        String groupName = session.getPathParameters().get("groupName");
        CtsGroup group = groupRepository.findByName(groupName);
        if (group == null) {
            session.getBasicRemote().sendText(Result.error("群不存在"));
            session.close();
            return;
        } else {
            session.getUserProperties().put("acceptGroup", group);
            // 获取群里的所有在线成员Session
            List<Session> list = groupSession.get(group.getAccount());
            if (list == null) {
                list = new ArrayList<>();
                groupSession.put(group.getAccount(), list);
            }
            list.add(session);

            String body = HttpUtil.get(session.getPathParameters().get("checkUrl"));
            JSONObject sendMember = JSON.parseObject(body);
            session.getUserProperties().put("sendMember", sendMember);
        }
    }

    @OnClose
    public void onClose(Session session) {
        CtsGroup group = (CtsGroup) session.getUserProperties().get("acceptGroup");
        if (groupSession.get(group.getAccount()) != null) {
            groupSession.get(group.getAccount()).remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
         try {
            JSONObject jsonObject = JSON.parseObject(message);
            String type = jsonObject.getString("type");
            Method method = groupSocketService.getClass().getDeclaredMethod(type, JSONObject.class, Session.class);
            method.invoke(groupSocketService, jsonObject, session);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
