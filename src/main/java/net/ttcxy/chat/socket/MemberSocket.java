package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.service.MemberSocketService;

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

        // 将所有的参数放入到pathParameters中
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().stream().findFirst().orElseThrow());
        }

        String checkUrl = session.getPathParameters().get("checkUrl");
        String username = session.getPathParameters().get("username");

        // 获取发送者的信息
        String body = HttpUtil.get(checkUrl);
        // 发送者的信息
        JSONObject sendMember = JSONObject.parseObject(body);
        // 接收者的信息
        CtsMember acceptMember = memberRepository.findByUsername(username);

        if (acceptMember == null) {
            session.getBasicRemote().sendText("用户不存在");
            session.close();
        } else {
            session.getUserProperties().put("acceptMember", acceptMember);
            session.getUserProperties().put("sendMember", sendMember);
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        try {
            switch (type) {
                case "join":
                    memberSocketService.join(jsonObject, session);
                    break;
                case "info":
                    memberSocketService.info(jsonObject, session);
                    break;
                case "message":
                    memberSocketService.message(jsonObject, session);
                    break;
                case "delete":
                    memberSocketService.delete(jsonObject, session);
                    break;  
                default:
                    System.out.println("喀什酱豆腐空间打开");
                    break;
            }
        } catch (Exception e) {
            System.out.println("******************************");
            System.out.println(e.getMessage());
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
