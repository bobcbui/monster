package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.model.CtsUser;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MessageRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;
import net.ttcxy.chat.repository.UserRepository;

/**
 * 本地用户接收消息使用
 */
@ServerEndpoint(value = "/local/{token}")
@Component
public class ApplicationSocket {

    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationUserRepository relationUserRepository;

    private static UserRepository userRepository;

    private static MessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MessageRepository messageRepository){
        ApplicationSocket.messageRepository = messageRepository;
    }


    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        ApplicationSocket.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        ApplicationSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        ApplicationSocket.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        ApplicationSocket.userRepository = userRepository;
    }

    public static Map<String,List<Session>> localSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        CtsUser user = ApplicationData.tokenUserMap.get(token);
        
        if(user == null){
            session.close();
            return;
        }

        List<Session> list = localSession.get(user.getUsername());

        if(list == null){
            list = new ArrayList<>();
            localSession.put(user.getUsername(), list);
        }
        
        list.add(session);
        session.getUserProperties().put("userData", user);
    }

    @OnClose
    public void onClose(Session session) {
        CtsUser user = (CtsUser)session.getUserProperties().get("userData");
        List<Session> localSession = ApplicationSocket.localSession.get(user.getUsername());
        localSession.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String token = session.getPathParameters().get("token");
        CtsUser user = ApplicationData.tokenUserMap.get(token);

        JSONObject parseObject = JSONObject.parseObject(message);
        String type = parseObject.getString("type");
        
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
