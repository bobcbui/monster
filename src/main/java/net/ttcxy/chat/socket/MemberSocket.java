package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.entity.CtsMemberRelation;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.service.MemberSocketService;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/member/{username}")
@Component
public class MemberSocket {

    private static Map<String, List<Session>> memberSession = new HashMap<>();

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberMessageRepository messageRepository;

    private static MemberRepository memberRepository;

    private static MemberSocketService memberSocketService;

    @Autowired
    public void setMemberMessageService(MemberSocketService memberSocketService) {
        MemberSocket.memberSocketService = memberSocketService;
    }

    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository) {
        MemberSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        MemberSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationMemberRepository) {
        MemberSocket.memberRelationRepository = relationMemberRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        MemberSocket.memberRepository = memberRepository;
    }


    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository) {
        MemberSocket.messageRepository = messageRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String checkUrl = session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);
        JSONObject parseObject = JSONObject.parseObject(body);

        String username = session.getPathParameters().get("username");

        CtsMember member = memberRepository.findByUsername(username);

        session.getUserProperties().put("acceptMember", member);
        session.getUserProperties().put("sendMember", parseObject);
    }

    @OnClose
    public void onClose(Session session) {

    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        switch(type){
            case "joinMember":
                memberSocketService.joinMemberHandler(jsonObject, session);
                break;
            case "searchMember":
                memberSocketService.searchMemberHandler(jsonObject, session);
                break;
            case "message":
                memberSocketService.messageHandler(jsonObject, session);
                break;
            default:
                System.out.println("喀什酱豆腐空间打开");
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
