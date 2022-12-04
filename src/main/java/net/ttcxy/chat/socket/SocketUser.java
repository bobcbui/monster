package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import net.ttcxy.chat.entity.ResultMap;
import net.ttcxy.chat.entity.model.CtsMessage;
import net.ttcxy.chat.entity.model.CtsRelationUser;
import net.ttcxy.chat.entity.model.CtsUser;
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

        String username = session.getPathParameters().get("username");

        CtsUser user = userRepository.findById(username).orElseThrow();

        session.getUserProperties().put("sendUserData", parseObject);
        session.getUserProperties().put("socketUser", user);
    }

    @OnClose
    public void onClose(Session session) {
        
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            String username = session.getPathParameters().get("username");
            JSONObject userData = (JSONObject)session.getUserProperties().get("sendUserData");
            CtsUser user = (CtsUser)session.getUserProperties().get("socketUser");
            
            JSONObject obj = JSONObject.parseObject(message);
            
            // 添加为好友
            if("add".equals(obj.getString("type"))){
                CtsRelationUser relationUser = new CtsRelationUser();
                relationUser.setNickname(userData.getString("username"));
                relationUser.setUsername(userData.getString("username"));;
                relationUser.setWs(userData.getString("ws"));
                relationUser.setPass(true);
                relationUserRepository.save(relationUser);
                session.getBasicRemote().sendText(new ResultMap("add", username).toJSON());
            }

            // 消息
            if("message-user".equals(obj.getString("type"))){
                CtsMessage message2 = new CtsMessage();
                message2.setName(user.getUsername());
                message2.setCreateTime(new Date());
                message2.setText(obj.getString("text"));
                message2.setType("message-user");
                message2.setWs(userData.getString("ws"));
                message2.setNickname(userData.getString("username"));
                messageRepository.save(message2);
                
                List<Session> localSession = SocketLocal.localSession.get(username);
                if(!user.getWs().equals(userData.getString("ws"))){
                    session.getBasicRemote().sendText(JSON.toJSONString(message2));
                }
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
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
