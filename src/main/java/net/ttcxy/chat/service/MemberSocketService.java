package net.ttcxy.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.entity.CtsMemberMessage;
import net.ttcxy.chat.entity.CtsMemberRelation;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.socket.LocalSocket;

@Service
public class MemberSocketService {

    @Autowired
    MemberRelationRepository memberRelationRepository;

    @Autowired
    MemberMessageRepository memberMessageRepository;

    public void joinMemberHandler(JSONObject data, Session session) {
        String type = data.getString("type");
        try {
            CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
            JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
            String username = sendMember.getString("username");
            String account = sendMember.getString("account");
            CtsMemberRelation memberRelation = new CtsMemberRelation();
            memberRelation.setId(IdUtil.objectId());
            memberRelation.setAccount(account);
            memberRelation.setNickname(username);
            memberRelation.setUsername(username);
            memberRelation.setCreateTime(DateUtil.date());
            memberRelation.setState(0);
            memberRelation.setMemberId(acceptMember.getId());
            memberRelationRepository.save(memberRelation);
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("member", acceptMember);
            session.getAsyncRemote().sendText(JSON.toJSONString(map));
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("message", e.getMessage());
            session.getAsyncRemote().sendText(JSON.toJSONString(e));
        }
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    public void searchMemberHandler(JSONObject data, Session session){
        String type = data.getString("type");
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("member", acceptMember);
        session.getAsyncRemote().sendText(JSON.toJSONString(map));
    }

    public void messageHandler(JSONObject data, Session session){
        String orderId = data.getString("orderId");
        String type = data.getString("type");
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        String account = sendMember.getString("account");
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("orderId", orderId);
        map.put("content", "success");
        session.getAsyncRemote().sendText(JSON.toJSONString(map));
        List<Session> sessionList = LocalSocket.localSession.get(acceptMember.getUsername());
        CtsMemberMessage memberMessage = new CtsMemberMessage();
        memberMessage.setId(IdUtil.objectId());
        memberMessage.setOrderId(orderId);
        memberMessage.setSendAccount(account);
        memberMessage.setAcceptAccount(acceptMember.getAccount());
        memberMessage.setCreateTime(DateUtil.date());
        memberMessage.setContent(data.getString("content"));
        memberMessage.setAccount(acceptMember.getAccount());
        memberMessage.setWithAccount(account);

        memberMessageRepository.save(memberMessage);
        if(sessionList == null){
            return;
        }
        for (Session list : sessionList) {
            JSONObject messageObject = new JSONObject();
            messageObject.put("type", "message");
            messageObject.put("orderId", orderId);
            messageObject.put("sendAccount", account);
            messageObject.put("content",data.getString("content"));
            list.getAsyncRemote().sendText(messageObject.toJSONString());
        }
    }
}
