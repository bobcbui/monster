package net.ttcxy.chat.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.entity.CurrentMember;
import net.ttcxy.chat.entity.model.Gang;
import net.ttcxy.chat.entity.model.GangMember;
import net.ttcxy.chat.entity.model.Member;
import net.ttcxy.chat.entity.model.Message;
import net.ttcxy.chat.security.jwt.TokenProvider;
import net.ttcxy.chat.service.GangService;
import net.ttcxy.chat.util.SpringUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * created by huanglei on 2020/09/18
 */
@ServerEndpoint(value = "/tang/ws",configurator = WebSocketConfig.class)
@Component
public class ApplicationWebSocket {

    // 成员ID的所有连接
    private static final Map<String,Set<ApplicationWebSocket>> memberSocketList = new HashMap<>();
    // SessionId对应的用户
    private static final Map<String, CurrentMember> sessionMember = new HashMap<>();
    // 这个群有哪些用户
    public static final Map<String, Set<String>> gangMember = new HashMap<>();
    // 这个群有哪些消息 最近 200 条消息
    private static final Map<String, List<JSONObject>> gangMessage = new HashMap<>();
    // gangId
    public static final Map<String, Gang> gangMap = new HashMap<>();


    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {

        this.session = session;

        TokenProvider tokenProvider = SpringUtil.getBean(TokenProvider.class);
        GangService gangService = SpringUtil.getBean(GangService.class);

        String jwt = (String) endpointConfig.getUserProperties().get("jwt");
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        CurrentMember member = (CurrentMember)authentication.getPrincipal();

        String memberId = member.getMember().getId();
        String sessionId = session.getId();

        sessionMember.put(sessionId,member);

        Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(memberId);
        if (applicationWebSockets == null){
            applicationWebSockets = new HashSet<>();
            applicationWebSockets.add(this);
            memberSocketList.put(memberId,applicationWebSockets);
        }else{
            applicationWebSockets.add(this);
        }

        // 将当前用户添加到所在的群
        List<GangMember> memberGangIdList = gangService.selectMemberGang(memberId);
        for (GangMember gm : memberGangIdList) {
            String gangId = gm.getGangId();
            Set<String> strings = gangMember.get(gangId);
            if (strings == null){
                strings = new HashSet<>();
                strings.add(memberId);
                gangMember.put(gangId, strings);
            }else{
                strings.add(memberId);
            }

            // 发送这个群的消息给他
            List<JSONObject> messages = gangMessage.get(gangId);
            if (messages != null){
                sendMessage(JSONObject.toJSONString(messages));
            }
        }

        System.out.printf("成功建立连接~ 当前在线人数为:%s%n", memberSocketList.size());
        System.out.println(this);
    }
    @OnClose
    public void onClose() {
        CurrentMember currentMember = sessionMember.get(session.getId());
        if (currentMember != null){
            Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(currentMember.getMember().getId());
            applicationWebSockets.remove(this);
            System.out.printf("成功关闭连接~ 当前在线人数为:%s%n", memberSocketList.size());
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        String sessionId = session.getId();
        CurrentMember currentMember = sessionMember.get(sessionId);
        // 消息发送人
        Member member = currentMember.getMember();
        JSONObject jsonObject = null;
        try{
            jsonObject = JSONObject.parseObject(message);
            // 发送到那个群组
            String to = jsonObject.getString("to");
            String messageText = jsonObject.getString("text");

            Gang gang = gangMap.get(to);


            List<JSONObject> lsMessage = gangMessage.get(to);
            JSONObject msg = new JSONObject();
            msg.put("to",to);
            msg.put("from",member.getId());
            msg.put("createTime",DateUtil.date());
            msg.put("text",messageText);
            msg.put("id",IdUtil.objectId());
            msg.put("type",1);
            msg.put("gangName",gang.getName());
            msg.put("gangId",gang.getId());
            if (lsMessage == null){
                lsMessage = new ArrayList<>(200);
                lsMessage.add(msg);
            }

            Set<String> gangMemberList = gangMember.get(to);

            List<JSONObject> messages = gangMessage.get(to);
            if (messages == null){
                messages = new ArrayList<>();
                messages.add(msg);
                gangMessage.put(to,messages);
            }else{
                messages.add(msg);
            }
            if (messages.size() > 100){
                messages.remove(0);
            }

            for (String memberId : gangMemberList) {
                Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(memberId);
                for (ApplicationWebSocket applicationWebSocket : applicationWebSockets) {
                    ArrayList<JSONObject> arrayList = new ArrayList<>();
                    arrayList.add(msg);
                    applicationWebSocket.sendMessage(JSONObject.toJSONString(arrayList));
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }



    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
    }
    /**
     * 发送消息每次发送一个List
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
