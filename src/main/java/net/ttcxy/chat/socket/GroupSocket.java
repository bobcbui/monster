package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.repository.MemberRepository;

/**
 * 建立群聊通道
 */
@ServerEndpoint(value = "/group/{groupName}")
@Component
public class GroupSocket {

    private static Map<String,List<Session>> groupSession = new HashMap<>();

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberRepository memberRepository;

    private static MemberMessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository){
        GroupSocket.messageRepository = messageRepository;
    }

    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository){
        GroupSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        GroupSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationMemberRepository){
        GroupSocket.memberRelationRepository = relationMemberRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        GroupSocket.memberRepository = memberRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println(groupRelationRepository);
        System.out.println(groupRepository);
        System.out.println(memberRelationRepository);
        System.out.println(memberRepository);
        System.out.println(messageRepository);

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String groupName = session.getPathParameters().get("groupName");
        CtsGroup group = groupRepository.findByName(groupName);
        session.getBasicRemote().sendText(JSONObject.toJSONString(group));

        List<Session> list = groupSession.get(groupName);
        if(list == null) {
            list = new ArrayList<>();
            groupSession.put(groupName, list);
        }
        list.add(session);

        String body = HttpUtil.get(session.getPathParameters().get("checkUrl"));
        session.getUserProperties().put("memberData", JSONObject.parseObject(body));
    }

    @OnClose
    public void onClose(Session session) {
        String groupName = session.getPathParameters().get("groupName");
        if(groupSession.get(groupName) == null){
            groupSession.get(groupName).remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
