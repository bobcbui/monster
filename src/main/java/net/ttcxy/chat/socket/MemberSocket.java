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

    private static MemberRepository memberRepository;

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

    private static MemberMessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository) {
        MemberSocket.messageRepository = messageRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println(memberSession);
        System.out.println(groupRelationRepository);
        System.out.println(groupRepository);
        System.out.println(memberRelationRepository);
        System.out.println(memberRepository);
        System.out.println(messageRepository);

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String checkUrl = session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);
        JSONObject parseObject = JSONObject.parseObject(body);

        String username = session.getPathParameters().get("username");

        CtsMember member = memberRepository.findByUsername(username);

        session.getUserProperties().put("member", member);
        session.getUserProperties().put("beMember", parseObject);
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        if (type.equals("joinMember")) {
            JSONObject beMember = (JSONObject) session.getUserProperties().get("beMember");
            String username = beMember.getString("username");
            String ws = beMember.getString("ws");

            CtsMemberRelation memberRelation = new CtsMemberRelation();
            memberRelation.setId(IdUtil.objectId());
            memberRelation.setWs(ws);
            memberRelation.setNickname(username);
            memberRelation.setUsername(username);
            memberRelation.setCreateTime(DateUtil.date());
            memberRelation.setState(0);
            memberRelation.setMemberId(member.getId());

            memberRelationRepository.save(memberRelation);

            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("member", member);
            session.getAsyncRemote().sendText(JSON.toJSONString(map));

        }
        if (type.equals("searchMember")) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("member", member);
            session.getAsyncRemote().sendText(JSON.toJSONString(map));
        }
        if (type.equals("message")) {
            Map<String, String> map = new HashMap<>();
            map.put("type", type);
            map.put("id", jsonObject.getString("id"));
            map.put("data", "success");
            session.getAsyncRemote().sendText(JSON.toJSONString(map));

            for (Session list : LocalSocket.localSession.get(member.getUsername())) {
                list.getAsyncRemote().sendText(message);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
