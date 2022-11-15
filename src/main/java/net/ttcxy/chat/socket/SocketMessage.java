package net.ttcxy.chat.socket;

import java.io.IOException;
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

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import net.ttcxy.chat.entity.ResultMap;
import net.ttcxy.chat.entity.model.CtsMessage;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.entity.model.CtsRelationUser;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MessageRepository;
import net.ttcxy.chat.repository.UserRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/{username}")
@Component
public class SocketMessage {

    
    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationUserRepository relationUserRepository;

    private static UserRepository userRepository;


    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        SocketMessage.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        SocketMessage.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        SocketMessage.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        SocketMessage.userRepository = userRepository;
    }

    private static MessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MessageRepository messageRepository){
        SocketMessage.messageRepository = messageRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String checkUrl =  session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);
        JSONObject obj = JSON.parseObject(body);
        String username = obj.getString("username");
        String ws = obj.getString("ws");

        session.getPathParameters().put("username", username);
        session.getPathParameters().put("ws", ws);

        if(StrUtil.isEmpty(username)){
            session.getAsyncRemote().sendText("{text:'用户不存在'}");
            session.close();
        }
        session.getUserProperties().put("userData", JSONObject.parseObject(body));
    }

    @OnClose
    public void onClose() {

    }

    private static Map<String,List<Session>> userSession = new HashMap<>();

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String username = session.getPathParameters().get("username");
        String ws = session.getPathParameters().get("ws");
        JSONObject obj = JSONObject.parseObject(message);
        
        // 添加为好友
        if("add".equals(obj.getString("type"))){
            CtsRelationUser relationUser = new CtsRelationUser();
            relationUser.setNickname(username);
            relationUser.setUsername(username);
            relationUser.setWs(ws);
            relationUser.setPass(true);
            relationUserRepository.save(relationUser);
        }

        // 消息
        if("message".equals(obj.getString("type"))){
            JSONObject userData = (JSONObject)session.getUserProperties().get("userData");

            CtsMessage message2 = new CtsMessage();
            message2.setCreateTime(new Date());
            message2.setText(obj.getString("text"));
            message2.setType("message");
            message2.setName(username);
            message2.setWs(ws);
            message2.setNickname(userData.getString("username"));
            messageRepository.save(message2);
            
            List<Session> localSession = SocketLocal.localSession.get(username);
            if(localSession != null){
                for (Session session2 : localSession) {
                    session2.getBasicRemote().sendText(JSON.toJSONString(message2));
                }
            }
            
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
