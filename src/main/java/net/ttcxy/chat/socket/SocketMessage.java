package net.ttcxy.chat.socket;

import java.io.IOException;
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
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
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
    public void onOpen(Session session) throws IOException {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String ws = session.getPathParameters().get("ws");
        String checkUrl =  session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);
        JSONObject obj = JSON.parseObject(body);
        String username = obj.getString("username");

        session.getPathParameters().put("username", username);

        if(StrUtil.isEmpty(username)){
            session.getAsyncRemote().sendText("{text:'用户不存在'}");
            session.close();
        }
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        
        JSONObject obj = JSONObject.parseObject(message);
        
        // 加入
        if("join".equals(obj.getString("type"))){
            CtsRelationGroup relationGroup = new CtsRelationGroup();
            relationGroup.setGroupName(session.getPathParameters().get("groupName"));
            relationGroup.setWs(session.getPathParameters().get("ws"));
            relationGroup.setPass(true);
            relationGroupRepository.save(relationGroup);
        }
        
        // 添加为好友
        if("add".equals(obj.getString("type"))){
            
        }

        // 删除好友
        if("delete".equals(obj.getString("type"))){
            
        }

        // 发送消息
        if("delete".equals(obj.getString("type"))){
            
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
