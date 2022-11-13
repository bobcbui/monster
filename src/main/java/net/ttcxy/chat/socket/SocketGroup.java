package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsMessage;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MessageRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;
import net.ttcxy.chat.repository.UserRepository;

/**
 * 建立群聊通道
 */
@ServerEndpoint(value = "/group/{groupName}")
@Component
public class SocketGroup {

    private static Map<String,List<Session>> groupSession = new HashMap<>();

    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationUserRepository relationUserRepository;

    private static UserRepository userRepository;

    private static MessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MessageRepository messageRepository){
        SocketGroup.messageRepository = messageRepository;
    }

    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        SocketGroup.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        SocketGroup.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        SocketGroup.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        SocketGroup.userRepository = userRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String groupName = session.getPathParameters().get("groupName");
        CtsGroup group = groupRepository.findByGroupName(groupName);
        session.getBasicRemote().sendText(JSONObject.toJSONString(group));

        List<Session> list = groupSession.get(groupName);
        if(list == null){
            list = new ArrayList<>();
            groupSession.put(groupName, list);
        }
        list.add(session);

        JSONObject message = new JSONObject();
        message.put("type", "message");
        message.put("messageName", "java学习交流群");

        session.getBasicRemote().sendText(message.toJSONString());
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject obj = JSON.parseObject(message);
            String type = obj.getString("type");
            System.out.println(message);
            String groupName = session.getPathParameters().get("groupName");
            String ws = session.getPathParameters().get("ws");

            if("join".equals(type)){
                CtsRelationGroup relationGroup = new CtsRelationGroup();
                relationGroup.setWs(session.getPathParameters().get("ws"));
                relationGroup.setGroupName(groupName);
                relationGroup.setPass(true);
                relationGroupRepository.save(relationGroup);
                session.getAsyncRemote().sendText("{'message':'加入成功'}");
            }


            // 发送消息给所有在线的用户
            if("message".equals(type)){
                List<Session> list = groupSession.get(groupName);
                for (Session list2 : list) {
                    try {
                        list2.getAsyncRemote().sendText(obj.toJSONString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        // TODO: handle exception
                    }
                    
                }
                CtsMessage message2 = new CtsMessage();
                message2.setCreateTime(new Date());
                message2.setText(obj.getString("text"));
                message2.setType("message");
                message2.setWs(ws);
                messageRepository.save(null);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
