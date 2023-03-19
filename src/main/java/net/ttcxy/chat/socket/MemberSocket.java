package net.ttcxy.chat.socket;

import java.io.IOException;
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
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/{username}")
@Component
public class MemberSocket {

    private static Map<String,List<Session>> userSession = new HashMap<>();

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberRepository memberRepository;


    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository){
        MemberSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        MemberSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationUserRepository){
        MemberSocket.memberRelationRepository = relationUserRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        MemberSocket.memberRepository = memberRepository;
    }

    private static MemberMessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository){
        MemberSocket.messageRepository = messageRepository;
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
        String checkUrl =  session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);

        JSONObject parseObject = JSONObject.parseObject(body);

        String username = session.getPathParameters().get("username");

        CtsMember member = memberRepository.findById(username).orElseThrow();

        session.getUserProperties().put("sendMemberData", parseObject);
        session.getUserProperties().put("memberSocket", member);
    }

    @OnClose
    public void onClose(Session session) {
        
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            String username = session.getPathParameters().get("username");
            JSONObject userData = (JSONObject)session.getUserProperties().get("sendMemberData");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
