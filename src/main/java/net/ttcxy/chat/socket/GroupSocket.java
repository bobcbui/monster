package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.code.ResultMap;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.service.GroupSocketService;

/**
 * 建立群聊通道
 */
@ServerEndpoint(value = "/group/{groupName}")
@Component
public class GroupSocket {

    private static GroupSocketService groupSocketService;

     private static Map<String, List<Session>> groupSession = new HashMap<>();

    private static GroupRepository groupRepository;

    @Autowired
    public void setGroupSocketService(GroupSocketService groupSocketService) {
        GroupSocket.groupSocketService = groupSocketService;
    }
   
    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        GroupSocket.groupRepository = groupRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String groupName = session.getPathParameters().get("groupName");
        CtsGroup group = groupRepository.findByName(groupName);
        if(group == null){
            session.getBasicRemote().sendText("群不存在");
            session.close();
            return;
        }else{
            // 
            session.getBasicRemote().sendText(ResultMap.result("groupInfo", group));
            // 获取群里的所有在线成员Session
            List<Session> list = groupSession.get(groupName);
            if (list == null) {
                list = new ArrayList<>();
                groupSession.put(groupName, list);
            }
            list.add(session);

            String body = HttpUtil.get(session.getPathParameters().get("checkUrl"));
            JSONObject sendMember = JSON.parseObject(body);
            session.getUserProperties().put("sendMember", sendMember);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String groupName = session.getPathParameters().get("groupName");
        if (groupSession.get(groupName) == null) {
            groupSession.get(groupName).remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSON.parseObject(message);
        String type = jsonObject.getString("type");
        try {
            switch (type) {
                case "groupInfo":
                    groupSocketService.groupInfo(jsonObject, session);
                    break;
                case "groupMember":
                    groupSocketService.groupMember(jsonObject, session);
                    break;
                case "groupMessage":
                    groupSocketService.groupMessage(jsonObject, session);
                    break;
                case "sendMessage":
                    groupSocketService.sendMessage(jsonObject, session);
                    break;
                case "notion":
                    groupSocketService.notion(jsonObject, session);
                    break;
                default:
                    System.out.println("喀什酱豆腐空间打开");
                    break;
            }
        } catch (Exception e) {
            System.out.println("ERROR:" + e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
