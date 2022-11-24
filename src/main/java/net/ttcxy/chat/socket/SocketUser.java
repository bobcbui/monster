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

import cn.hutool.http.HttpUtil;
import net.ttcxy.chat.entity.ResultMap;
import net.ttcxy.chat.entity.model.CtsMessage;
import net.ttcxy.chat.entity.model.CtsRelationUser;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MessageRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationUserRepository;
import net.ttcxy.chat.repository.UserRepository;

/**
 * 发送消息使用
 */
@ServerEndpoint(value = "/{username}")
@Component
public class SocketUser {

    private static Map<String,List<Session>> userSession = new HashMap<>();

    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationUserRepository relationUserRepository;

    private static UserRepository userRepository;


    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        SocketUser.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        SocketUser.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationUserRepository(RelationUserRepository relationUserRepository){
        SocketUser.relationUserRepository = relationUserRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        SocketUser.userRepository = userRepository;
    }

    private static MessageRepository messageRepository;

    @Autowired
    public void setRelationGroupRepository(MessageRepository messageRepository){
        SocketUser.messageRepository = messageRepository;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        for (Map.Entry<String, List<String>> me : requestParameterMap.entrySet()) {
            session.getPathParameters().put(me.getKey(), me.getValue().get(0));
        }
        String checkUrl =  session.getPathParameters().get("checkUrl");

        String body = HttpUtil.get(checkUrl);

        JSONObject parseObject = JSONObject.parseObject(body);

        session.getUserProperties().put("userData", parseObject);

    }

    @OnClose
    public void onClose(Session session) {
        
    }

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
            if("message-user".equals(obj.getString("type"))){
                CtsMessage message2 = new CtsMessage();
                message2.setName(username);
                message2.setCreateTime(new Date());
                message2.setText(obj.getString("text"));
                message2.setType("message-user");
                message2.setWs(userData.getString("ws"));
                message2.setNickname(userData.getString("username"));
                messageRepository.save(message2);
                
                List<Session> localSession = SocketLocal.localSession.get(username);
                session.getBasicRemote().sendText(JSON.toJSONString(message2));
                if(localSession != null){
                    for (Session session2 : localSession) {
                        try {
                            session2.getBasicRemote().sendText(JSON.toJSONString(message2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
