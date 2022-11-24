package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.ResultMap;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsMessage;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.entity.model.CtsRelationUser;
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
public class SocketLocal {

    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationUserRepository relationUserRepository;

    private static UserRepository userRepository;

    private static MessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MessageRepository messageRepository){
        SocketLocal.messageRepository = messageRepository;
    }


    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        SocketLocal.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        SocketLocal.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        SocketLocal.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        SocketLocal.userRepository = userRepository;
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
        List<Session> localSession = SocketLocal.localSession.get(user.getUsername());
        localSession.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String token = session.getPathParameters().get("token");
        CtsUser user = ApplicationData.tokenUserMap.get(token);

        JSONObject parseObject = JSONObject.parseObject(message);
        String type = parseObject.getString("type");
        switch(type){
            case "groupList":
                List<CtsRelationGroup> relationGroupList =  relationGroupRepository.findByWs(user.getWs());
                List<CtsGroup> groupList = relationGroupList.stream().map(CtsRelationGroup::getGroup).collect(Collectors.toList());
                session.getBasicRemote().sendText(new ResultMap("groupList",groupList).toJSON());
            break;
            case "userList":
                List<CtsRelationUser> relationUserList =  relationUserRepository.findByUsername(user.getUsername());
                session.getBasicRemote().sendText(new ResultMap("userList",relationUserList).toJSON());
            break;
            case "messageList":
                List<String> strings = relationUserRepository.findByUsername(user.getUsername()).stream().map(CtsRelationUser::getNickname).collect(Collectors.toList());
                List<CtsMessage> messages = new ArrayList<CtsMessage>();
                for (String nickname : strings) {
                    List<CtsMessage> msgs = messageRepository.findByNameAndWs(nickname, user.getWs());
                    messages.addAll(msgs);
                }
                session.getBasicRemote().sendText(new ResultMap("messageList",messages).toJSON());
            break;
            case "add":
                CtsRelationUser relationUser = new CtsRelationUser();
                relationUser.setNickname(parseObject.getString("username"));
                relationUser.setWs(parseObject.getString("ws"));
                relationUser.setPass(true);
                relationUser.setUsername(user.getUsername());
                relationUserRepository.save(relationUser);
            break;
            case "message-user":
                CtsMessage message2 = new CtsMessage();
                message2.setName(user.getUsername());
                message2.setNickname(parseObject.getString("nickname"));
                message2.setText(parseObject.getString("text"));
                message2.setCreateTime(new Date());
                message2.setType(parseObject.getString("type"));
                message2.setWs(parseObject.getString("ws"));
                messageRepository.save(message2);
            break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
