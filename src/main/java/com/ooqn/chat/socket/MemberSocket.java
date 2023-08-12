package com.ooqn.chat.socket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.repository.MemberRepository;
import com.ooqn.chat.service.MemberSocketService;

import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/member/{username}")
@Component
public class MemberSocket {

    private static MemberRepository memberRepository;

    private static MemberSocketService memberSocketService;

    @Autowired
    public void setMemberMessageService(MemberSocketService memberSocketService) {
        MemberSocket.memberSocketService = memberSocketService;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        MemberSocket.memberRepository = memberRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().stream().findFirst().orElseThrow());
        }
        
        String username = session.getPathParameters().get("username");

        // 接收者的信息
        CtsMember acceptMember = memberRepository.findByUsername(username);
        if (acceptMember == null) {
            session.getBasicRemote().sendText("用户不存在");
            session.close();
        } else {
            // 获取发送者的信息
            String checkUrl = session.getPathParameters().get("checkUrl");
            String body = HttpUtil.get(checkUrl);
            // 发送者的信息
            JSONObject sendMember = JSONObject.parseObject(body);
            session.getUserProperties().put("acceptMember", acceptMember);
            session.getUserProperties().put("sendMember", sendMember);
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            String type = jsonObject.getString("type");
            Method method = memberSocketService.getClass().getDeclaredMethod(type, JSONObject.class, Session.class);
            method.invoke(memberSocketService, jsonObject, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
