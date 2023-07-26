package com.ooqn.chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.entity.CtsMemberMessage;
import com.ooqn.chat.entity.CtsMemberRelation;
import com.ooqn.chat.entity.CtsVerify;
import com.ooqn.chat.repository.MemberMessageRepository;
import com.ooqn.chat.repository.MemberRelationRepository;
import com.ooqn.chat.repository.VerifyRepository;
import com.ooqn.chat.socket.LocalSocket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;

@Service
public class MemberSocketService {

    @Autowired
    private MemberRelationRepository memberRelationRepository;

    @Autowired
    private MemberMessageRepository memberMessageRepository;

    @Autowired
    private VerifyRepository verifyRepository;
    

    public void join(JSONObject data, Session session) {
        String type = data.getString("type");
        String transactionId = data.getString("transactionId");
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

            JSONObject dataJson = new JSONObject();
            dataJson.put("account", account);
            dataJson.put("context", data.getString("context"));
            dataJson.put("username", username);

            CtsVerify verify = new CtsVerify();
            String verifyId = IdUtil.objectId();
            verify.setId(verifyId);
            verify.setMemberId(acceptMember.getId());
            verify.setCreateTime(DateUtil.date());
            verify.setUpdateTime(DateUtil.date());
            verify.setState(0);
            verify.setType("1");
            verify.setData(dataJson.toJSONString());
            verifyRepository.save(verify);

            session.getAsyncRemote().sendText(Result.r(transactionId, type, Result.success ,acceptMember));
        } catch (Exception e) {
            session.getAsyncRemote().sendText(Result.r(transactionId, type , Result.success, e.getMessage()));
        }
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    public void info(JSONObject data, Session session){
        String type = data.getString("type");
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), type, Result.success, acceptMember));
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
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), type, Result.success, serviceId));

        if(sessionList != null){
            for (Session list : sessionList) {
                if(list.isOpen()){
                    list.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "message", Result.success,memberMessage));
                }
            }
        }
    }

    public void delete(JSONObject data, Session session) {
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        memberRelationRepository.deleteByMemberIdAndAccount(acceptMember.getId(), sendMember.getString("account"));
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "delete", Result.success,sendMember.getString("accept")));
    }

    public void agree(JSONObject data, Session session) {
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        String verifyId = data.getString("verifyId");
        
        CtsMemberRelation memberRelation = memberRelationRepository.findByMemberIdAndAccount(acceptMember.getId(), sendMember.getString("account"));
        memberRelation.setState(1);
        memberRelation.setUpdateTime(DateUtil.date());
        memberRelationRepository.save(memberRelation);

        Optional<CtsVerify> verify = verifyRepository.findById(verifyId);
        verify.get().setState(4);
        verify.get().setUpdateTime(DateUtil.date());
        verifyRepository.save(verify.get());
        
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "agree", Result.success));
    }

    public void reject(JSONObject data, Session session) {
        CtsMember acceptMember = (CtsMember) session.getUserProperties().get("acceptMember");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        String verifyId = data.getString("verifyId");
        
        CtsMemberRelation memberRelation = memberRelationRepository.findByMemberIdAndAccount(acceptMember.getId(), sendMember.getString("account"));
        memberRelation.setState(1);
        memberRelation.setUpdateTime(DateUtil.date());
        memberRelationRepository.save(memberRelation);

        Optional<CtsVerify> verify = verifyRepository.findById(verifyId);
        verify.get().setState(5);
        verify.get().setUpdateTime(DateUtil.date());
        verifyRepository.save(verify.get());
        
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "agree", Result.success));
    }
}
