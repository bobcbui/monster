package net.ttcxy.chat.config;

import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.db.tables.pojos.Member;
import net.ttcxy.chat.entity.CurrentMember;
import net.ttcxy.chat.security.jwt.TokenProvider;
import net.ttcxy.chat.service.GroupService;
import net.ttcxy.chat.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Map<String, Set<String>> groupMember = new HashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        TokenProvider tokenProvider = SpringUtil.getBean(TokenProvider.class);
        GroupService groupService = SpringUtil.getBean(GroupService.class);

        String jwt = (String) endpointConfig.getUserProperties().get("jwt");
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        CurrentMember member = (CurrentMember)authentication.getPrincipal();
        String memberId = member.getMember().getId();
        this.session = session;
        String id = session.getId();
        sessionMember.put(id,member);

        Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(memberId);
        if (applicationWebSockets == null){
            applicationWebSockets = new HashSet<>();
            applicationWebSockets.add(this);
            memberSocketList.put(memberId,applicationWebSockets);
        }else{
            applicationWebSockets.add(this);
        }

        // 将当前用户添加到所在的群
        List<String> memberGroupIdList = groupService.selectMemberGroup(memberId);
        for (String groupId : memberGroupIdList) {
            Set<String> strings = groupMember.get(groupId);
            if (strings == null){
                strings = new HashSet<>();
                strings.add(memberId);
                groupMember.put(groupId, strings);
            }else{
                strings.add(memberId);
            }
        }

        System.out.printf("成功建立连接~ 当前在线人数为:%s%n", memberSocketList.size());
        System.out.println(this);
    }
    @OnClose
    public void onClose() {
        CurrentMember currentMember = sessionMember.get(session.getId());
        Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(currentMember.getMember().getId());
        applicationWebSockets.remove(this);
        System.out.printf("成功关闭连接~ 当前在线人数为:%s%n", memberSocketList.size());
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
            String to = jsonObject.getString("to");// 接收人，群组
            String type = jsonObject.getString("type");
            String messageText = jsonObject.getString("messageText");

            if ("group".equals(type)){
                Set<String> groupMemberList = groupMember.get(to);
                for (String memberId : groupMemberList) {
                    Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(memberId);
                    for (ApplicationWebSocket applicationWebSocket : applicationWebSockets) {
                        JSONObject from = new JSONObject();
                        from.put("from",to);
                        from.put("memberId",member.getId());
                        from.put("type","groupMessage");
                        from.put("time",System.currentTimeMillis());
                        from.put("messageText",messageText);
                        applicationWebSocket.sendMessage(from.toJSONString());
                    }
                }
            }else if ("member".equals(type)){
                Set<ApplicationWebSocket> applicationWebSockets = memberSocketList.get(member.getId());
                for (ApplicationWebSocket applicationWebSocket : applicationWebSockets) {
                    JSONObject from = new JSONObject();
                    from.put("from",to);
                    from.put("memberId",member.getId());
                    from.put("type","memberMessage");
                    from.put("time",System.currentTimeMillis());
                    from.put("messageText",messageText);
                    applicationWebSocket.sendMessage(from.toJSONString());
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
     * 指定发消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
