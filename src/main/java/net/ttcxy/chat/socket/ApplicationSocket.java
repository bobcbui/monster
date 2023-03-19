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
import jakarta.websocket.server.PathParam;
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
@ServerEndpoint(value = "/local/{token}")
@Component
public class ApplicationSocket {

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberRepository memberRepository;

    private static MemberMessageRepository memberMessageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository){
        ApplicationSocket.memberMessageRepository = messageRepository;
    }


    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository){
        ApplicationSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        ApplicationSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationUserRepository){
        ApplicationSocket.memberRelationRepository = relationUserRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository userRepository){
        ApplicationSocket.memberRepository = userRepository;
    }

    public static Map<String,List<Session>> localSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        System.out.println(groupRelationRepository);
        System.out.println(groupRepository);
        System.out.println(memberRelationRepository);
        System.out.println(memberRepository);
        System.out.println(memberMessageRepository);


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
    }

    @OnClose
    public void onClose(Session session) {
        CtsMember member = (CtsMember)session.getUserProperties().get("memberData");
        if(member != null){
            List<Session> localSession = ApplicationSocket.localSession.get(member.getName());
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
