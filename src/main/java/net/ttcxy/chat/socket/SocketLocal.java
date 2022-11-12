package net.ttcxy.chat.socket;

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
import net.ttcxy.chat.entity.model.CtsUser;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.entity.model.CtsRelationUser;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.UserRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;

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
        this.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        this.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {

        CtsUser user = ApplicationData.tokenUserMap.get(token);

        List<CtsRelationGroup> relationGroupList =  relationGroupRepository.findByWs(user.getWs());
        List<CtsRelationUser> relationUserList =  relationUserRepository.findByUsername(user.getUsername());

        List<CtsGroup> groupList = new ArrayList<>();
        for (CtsRelationGroup relationGroup : relationGroupList) {
            String groupName = relationGroup.getGroupName();
            groupList.add(groupRepository.findByGroupName(groupName));
        }
        Map<String,Object> map = new HashMap<>(); 
        map.put("type", "groupList");
        map.put("data", groupList);
        session.getAsyncRemote().sendText(JSONObject.toJSONString(map));

        System.out.println("skdjfksdf");
        
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
