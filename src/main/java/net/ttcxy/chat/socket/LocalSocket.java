package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.repository.MemberRepository;

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
    public void setRelationGroupRepository(MemberMessageRepository messageRepository){
        LocalSocket.memberMessageRepository = messageRepository;
    }


    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository){
        LocalSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        LocalSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationUserRepository){
        LocalSocket.memberRelationRepository = relationUserRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        LocalSocket.memberRepository = memberRepository;
    }

    public static Map<String,List<Session>> localSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {

        String token = session.getRequestParameterMap().get("token").get(0);

        CtsMember member = ApplicationData.tokenMemberMap.get(token);
        
        if(member == null){
            session.close();
            return;
        }

        List<Session> list = localSession.get(member.getName());

        if(list == null){
            list = new ArrayList<>();
            localSession.put(member.getName(), list);
        }
        
        list.add(session);
        session.getUserProperties().put("memberData", member);
        session.getAsyncRemote().sendText("连接成功");
    }

    @OnClose
    public void onClose(Session session) {
        CtsMember member = (CtsMember)session.getUserProperties().get("memberData");
        if(member != null){
            List<Session> localSession = LocalSocket.localSession.get(member.getName());
            localSession.remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
