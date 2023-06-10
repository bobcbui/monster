package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.entity.CtsGroupRelation;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.entity.CtsMemberMessage;
import net.ttcxy.chat.entity.CtsMemberRelation;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.service.LocalSocketService;

/**
 * 本地用户接收消息使用
 */
@ServerEndpoint(value = "/local")
@Component
public class LocalSocket {

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberRepository memberRepository;

    private static MemberMessageRepository memberMessageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository) {
        LocalSocket.memberMessageRepository = messageRepository;
    }

    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository) {
        LocalSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        LocalSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationUserRepository) {
        LocalSocket.memberRelationRepository = relationUserRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        LocalSocket.memberRepository = memberRepository;
    }

    private static LocalSocketService localSocketService;

    @Autowired
    public void setLocalSocketService(LocalSocketService localSocketService) {
        LocalSocket.localSocketService = localSocketService;
    }

    public static Map<String, List<Session>> localSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        try {
            String token = session.getRequestParameterMap().get("token").get(0);
            Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
            requestParameterMap.entrySet().forEach(e -> {
                session.getUserProperties().put(e.getKey(), e.getValue().get(0));
            });
            CtsMember member = ApplicationData.tokenMemberMap.get(token);
            if (member == null) {
                session.getAsyncRemote().sendText(resultMap("error", "token失效"));
                session.close();
                return;
            }
            List<Session> list = localSession.get(member.getUsername());
            if (list == null) {
                list = new ArrayList<>();
                localSession.put(member.getUsername(), list);
            }

            list.add(session);
            session.getUserProperties().put("memberData", member);
            session.getAsyncRemote().sendText(resultMap("system", "连接成功"));
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

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            JSONObject data = JSONObject.parseObject(message);
            String type = data.getString("type");
            switch (type) {
                case "groupList":
                   localSocketService.groupListHandler(data, session);
                    break;
                case "memberList":
                    localSocketService.memberListHandler(data, session);
                    break;
                case "memberMessage":
                    localSocketService.memberMessageHandler(data, session);
                    break;
                case "saveMemberMessage":
                    localSocketService.saveMemberHandler(data, session);
                    break;
                case "addMember":
                    localSocketService.addMemberHandler(data, session);
                    break;
                case "createGroup":
                    localSocketService.createGroupHandler(data, session);
                    break;
                default:
                    System.out.println("未知类型");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    private String resultMap(String type, String data) {
        Map<String, String> result = new HashMap<>();
        result.put("type", type);
        result.put("data", data);
        return JSONObject.toJSONString(result);
    }

    private List<Session> getSessionList(String member) {
        // 获取用户的SessionList
        List<Session> list = localSession.get(member);

        if (list == null) {
            list = new ArrayList<>();
            localSession.put(member, list);
        }
        return list;
    }
}
