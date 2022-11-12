package net.ttcxy.chat.socket;

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

import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;
import net.ttcxy.chat.repository.UserRepository;

/**
 * 建立群聊通道
 */
@ServerEndpoint(value = "/group")
@Component
public class SocketGroup {

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
    public void onOpen(Session session) {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String groupName = session.getPathParameters().get("groupName");
        CtsGroup group = groupRepository.findByGroupName(groupName);
        session.getAsyncRemote().sendText(JSONObject.toJSONString(group));
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject obj = JSON.parseObject(message);
        String type = obj.getString("type");
        System.out.println(message);

        if("join".equals(type)){
            CtsRelationGroup relationGroup = new CtsRelationGroup();
            relationGroup.setWs(session.getPathParameters().get("ws"));
            relationGroup.setGroupName(session.getPathParameters().get("groupName"));
            relationGroup.setPass(true);
            relationGroupRepository.save(relationGroup);
            session.getAsyncRemote().sendText("{'message':'加入成功'}");
        }

        // 发送消息给所有在线的用户

        // 保存消息

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
