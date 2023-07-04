package net.ttcxy.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
import net.ttcxy.chat.code.ResultMap;
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

    public void join(JSONObject data, Session session) {
        String serviceId = data.getString("serviceId");
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
            session.getAsyncRemote().sendText(ResultMap.result(type ,serviceId , acceptMember));
        } catch (Exception e) {
            session.getAsyncRemote().sendText(ResultMap.result(type ,serviceId , e.getMessage()));
        }
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    public void info(JSONObject data, Session session){
        String serviceId = data.getString("serviceId");
        String type = data.getString("type");
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        session.getAsyncRemote().sendText(ResultMap.result(type,serviceId , acceptMember));
    }

    public void message(JSONObject data, Session session){
        String serviceId = data.getString("serviceId");
        String type = data.getString("type");
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        String account = sendMember.getString("account");

        List<Session> sessionList = LocalSocket.localSession.get(acceptMember.getUsername());
        CtsMemberMessage memberMessage = new CtsMemberMessage();
        memberMessage.setId(IdUtil.objectId());
        memberMessage.setServiceId(serviceId);
        memberMessage.setSendAccount(account);
        memberMessage.setAcceptAccount(acceptMember.getAccount());
        memberMessage.setCreateTime(DateUtil.date());
        memberMessage.setContent(data.getString("content"));
        memberMessage.setAccount(acceptMember.getAccount());
        memberMessage.setWithAccount(account);
        memberMessageRepository.save(memberMessage);
        session.getAsyncRemote().sendText(ResultMap.result(type, serviceId , "success"));

        if(sessionList != null){
            for (Session list : sessionList) {
                if(list.isOpen()){
                    list.getAsyncRemote().sendText(ResultMap.result("message",serviceId , memberMessage));
                }
                
            }
        }
    }
}
