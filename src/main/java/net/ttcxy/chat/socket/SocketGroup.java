package net.ttcxy.chat.socket;

import java.util.Optional;

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

import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.repository.GroupRepository;

/**
 * 建立群聊通道
 */
@ServerEndpoint(value = "/group/{id}")
@Component
public class SocketGroup {

    private static GroupRepository groupRepository;

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        SocketGroup.groupRepository = groupRepository;
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("id")String groupId) {
        session.getPathParameters().put("groupId", groupId);
        Optional<CtsGroup> group = groupRepository.findById(Long.parseLong(groupId));
        session.getAsyncRemote().sendText(JSONObject.toJSONString(group.orElse(null)));
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        // cmd == load // 加载最近的消息

        // 发送消息给所有在线的用户

        // 保存消息

    }

    @OnError
    public void onError(Session session, Throwable error) {

    }
}
