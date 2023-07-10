package com.ooqn.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsGroup;
import com.ooqn.chat.repository.GroupRepository;
import com.ooqn.chat.service.GroupSocketService;

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
            session.getBasicRemote().sendText(Result.r("error", Result.success, "群不存在"));
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
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        try {
            switch (type) {
                case "join":
                    groupSocketService.join(jsonObject, session);
                    break;
                case "info":
                    groupSocketService.info(jsonObject, session);
                    break;
                case "members":
                    groupSocketService.members(jsonObject, session);
                    break;
                case "message":
                    groupSocketService.message(jsonObject, session);
                    break;
                case "messages":
                    groupSocketService.messages(jsonObject, session);
                    break;
                case "notion":
                    groupSocketService.notion(jsonObject, session);
                    break;
                default:
                    System.out.println("喀什酱豆腐空间打开");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR:" + e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
