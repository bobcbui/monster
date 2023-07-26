package com.ooqn.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.code.ApplicationData;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.service.LocalSocketService;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/local")
@Component
public class LocalSocket {

    public static Map<String, List<Session>> localSession = new HashMap<>();

    private static LocalSocketService localSocketService;

    @Autowired
    public void setLocalSocketService(LocalSocketService localSocketService) {
        LocalSocket.localSocketService = localSocketService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        try {
            String token = session.getRequestParameterMap().get("token").stream().findFirst().orElseThrow();
            Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
            requestParameterMap.entrySet().forEach(e -> {
                session.getUserProperties().put(e.getKey(), e.getValue().stream().findFirst().orElseThrow());
            });
            CtsMember member = ApplicationData.tokenMemberMap.get(token);
            if (member == null) {
                session.getAsyncRemote().sendText(Result.r("error", Result.success, "token失效"));
                session.close();
                return;
            }
            List<Session> list = localSession.get(member.getUsername());
            if (list == null) {
                list = new ArrayList<>();
                localSession.put(member.getUsername(), list);
            }

            list.add(session);
            session.getUserProperties().put("member", member);
            session.getAsyncRemote().sendText(Result.r("system", Result.success, "连接成功"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            JSONObject data = JSONObject.parseObject(message);
            String type = data.getString("type");
            switch (type) {
                case "loadVerify":
                    localSocketService.loadVerify(data, session);
                    break;
                case "groupList":
                    localSocketService.groupList(data, session);
                    break;
                case "memberMap":
                    localSocketService.memberMap(data, session);
                    break;
                case "memberMessage":
                    localSocketService.memberMessage(data, session);
                    break;
                case "saveMemberMessage":
                    localSocketService.saveMember(data, session);
                    break;
                case "joinMember":
                    localSocketService.joinMember(data, session);
                    break;
                case "createGroup":
                    localSocketService.createGroup(data, session);
                    break;
                case "saveMessage":
                    localSocketService.saveMessage(data, session);
                    break;
                case "loadMemberMessage":
                    localSocketService.loadMemberMessage(data, session);
                    break;
                case "loadMessage":
                    localSocketService.loadMessage(data, session);
                    break;
                case "deleteMember":
                    localSocketService.deleteMember(data, session);
                    break;
                case "deleteVerify":
                    localSocketService.deleteVerify(data, session);
                    break;
                case "agreeVerify":
                    localSocketService.agreeVerify(data, session);
                    break;
                case "rejectVerify":
                    localSocketService.rejectVerify(data, session);
                    break;
                default:
                    System.out.println("未知类型:" + type);
                    session.getAsyncRemote().sendText(data.toJSONString());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClose
    public void onClose(Session session) {
        try {
            CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
            if (member != null) {
                List<Session> localSession = LocalSocket.localSession.get(member.getUsername());
                localSession.remove(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

}
