package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
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
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.entity.model.CtsRelationUser;
import net.ttcxy.chat.entity.model.CtsUser;
import net.ttcxy.chat.repository.GroupRepository;
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
    }

    @OnClose
    public void onClose() {

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
        }



        



    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
