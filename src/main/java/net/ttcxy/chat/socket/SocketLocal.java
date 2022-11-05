package net.ttcxy.chat.socket;

import java.util.List;

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
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;
import net.ttcxy.chat.repository.RelationMemberRepository;

/**
 * 本地用户接收消息使用
 */
@ServerEndpoint(value = "/local/{token}")
@Component
public class SocketLocal {

    private static RelationGroupRepository relationGroupRepository;

    private static GroupRepository groupRepository;

    private static RelationMemberRepository relationMemberRepository;

    private static MemberRepository memberRepository;


    @Autowired
    public void setRelationGroupRepository(RelationGroupRepository relationGroupRepository){
        this.relationGroupRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setRelationMemberRepository(RelationMemberRepository relationMemberRepository){
        this.relationMemberRepository = relationMemberRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        CtsMember member = ApplicationData.tokenMemberMap.get(token);

        List<CtsRelationGroup> relationGroupList =  relationGroupRepository.findByMemberId(member.getId());
        List<CtsRelationGroup> relationGroupList =  relationMemberRepository.findByMemberId(member.getId());



        // message list
        session.getAsyncRemote().sendText("[]");
        // group list
        session.getAsyncRemote().sendText("[]");
        // member list
        session.getAsyncRemote().sendText("[]");
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
