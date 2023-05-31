package net.ttcxy.chat.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.entity.CtsMemberMessage;
import net.ttcxy.chat.entity.CtsMemberRelation;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;
import net.ttcxy.chat.repository.MemberRepository;

/**
 * 本地用户接收消息使用
 */
@ServerEndpoint(value = "/local")
@Component
public class LocalSocket {

    private static GroupRelationRepository groupRelationRepository;

    private static GroupRepository groupRepository;

    private static MemberRelationRepository memberRelationRepository;

    private static MemberRepository memberRepository;

    private static MemberMessageRepository memberMessageRepository;

    @Autowired
    public void setRelationGroupRepository(MemberMessageRepository messageRepository){
        LocalSocket.memberMessageRepository = messageRepository;
    }


    @Autowired
    public void setGroupRelationRepository(GroupRelationRepository relationGroupRepository){
        LocalSocket.groupRelationRepository = relationGroupRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository){
        LocalSocket.groupRepository = groupRepository;
    }

    @Autowired
    public void setMemberRelationRepository(MemberRelationRepository relationUserRepository){
        LocalSocket.memberRelationRepository = relationUserRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        LocalSocket.memberRepository = memberRepository;
    }

    public static Map<String,List<Session>> localSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        String token = session.getRequestParameterMap().get("token").get(0);
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        requestParameterMap.entrySet().forEach(e -> {
            session.getUserProperties().put(e.getKey(), e.getValue().get(0));
        });
        CtsMember member = ApplicationData.tokenMemberMap.get(token);
        if(member == null){
            session.getAsyncRemote().sendText(resultMap("error","token失效"));
            session.close();
            return;
        }
        List<Session> list = localSession.get(member.getUsername());
        if(list == null){
            list = new ArrayList<>();
            localSession.put(member.getUsername(), list);
        }
        
        list.add(session);
        session.getUserProperties().put("memberData", member);
        session.getAsyncRemote().sendText(resultMap("system","连接成功"));
        session.getAsyncRemote().sendText(resultMap("message","连接成功"));
    }

    @OnClose
    public void onClose(Session session) {
        CtsMember member = (CtsMember)session.getUserProperties().get("memberData");
        if(member != null){
            List<Session> localSession = LocalSocket.localSession.get(member.getUsername());
            localSession.remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject parse = JSONObject.parseObject(message);
        CtsMember member = (CtsMember)session.getUserProperties().get("memberData");
        String type = parse.getString("type");
        Map<String,String> result = new HashMap<>();
        try{
            switch(type){
                case "groupList":
                    // 获取群组列表
                    List<CtsGroup> groupList = groupRepository.findGroupByMemberId(member.getId());
                    result.put("type", "groupList");
                    result.put("data", JSON.toJSONString(groupList));
                    session.getBasicRemote().sendText(JSON.toJSONString(result));
                break;
                case "memberList":
                    // 获取成员列表
                    List<CtsMember> memberList = memberRepository.findMemberByMemberId(member.getId());
                    result.put("type", "memberList");
                    result.put("data", JSON.toJSONString(memberList));
                    session.getBasicRemote().sendText(JSON.toJSONString(result));
                    
                break;
                case "memberMessage":
                    // 获取成员的聊天记录
                    List<CtsMemberMessage> memberMessageList = memberMessageRepository.findMemberMessageByMemberId(member.getId());                
                break;
                case "saveMemberMessage":
                // 保存成员的聊天记录 
                    
                break;
                case "addMember":
                    JSONObject memberObject = parse.getJSONObject("data");
                    CtsMemberRelation memberRelation = new CtsMemberRelation();
                    memberRelation.setMemberId(member.getId());
                    memberRelation.setId(IdUtil.objectId());
                    memberRelation.setCreateTime(DateUtil.date());
                    memberRelation.setUsername(memberObject.getString("username"));
                    memberRelation.setNickname(memberObject.getString("username"));
                    memberRelation.setWs(memberObject.getString("ws"));
                    memberRelationRepository.save(memberRelation);
                break;
            }
        }catch(Exception e){

        }
        
    }

    @OnError
    public void onError(Session session, Throwable error) {

    }

    private String resultMap(String type,String data){
        Map<String,String> result = new HashMap<>();
        result.put("type", type);
        result.put("data", data);
        return JSONObject.toJSONString(result);
    }

    

    private List<Session> getSessionList(String member){
        // 获取用户的SessionList 
        List<Session> list = localSession.get(member);

        if(list == null){
            list = new ArrayList<>();
            localSession.put(member, list);
        }
        return list;
    }
}
