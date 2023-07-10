package com.ooqn.chat.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsGroup;
import com.ooqn.chat.entity.CtsGroupRelation;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.entity.CtsMemberMessage;
import com.ooqn.chat.entity.CtsMemberRelation;
import com.ooqn.chat.repository.GroupRelationRepository;
import com.ooqn.chat.repository.GroupRepository;
import com.ooqn.chat.repository.MemberMessageRepository;
import com.ooqn.chat.repository.MemberRelationRepository;

@Service
public class LocalSocketService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MemberRelationRepository memberRelationRepository;

    @Autowired
    private MemberMessageRepository memberMessageRepository;

    @Autowired
    private GroupRelationRepository groupRelationRepository;

    /**
     * 指令处理器
     */
    public void groupList(JSONObject data, Session session) throws IOException {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsGroupRelation> groupRelationList = groupRelationRepository.findByMemberAccount(member.getAccount());
        JSONArray jsonArray = new  JSONArray();
        for (CtsGroupRelation groupRelation : groupRelationList) {
            jsonArray.add(groupRelation.getGroupAccount());
        }
        session.getBasicRemote().sendText(Result.r("groupList", Result.success, jsonArray));
    }

    public void memberMap(JSONObject data, Session session) throws IOException {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberId(member.getId());
        // memberList to Map 
        Map<String,CtsMemberRelation> likeMap = new LinkedHashMap<>();
        CtsMemberRelation thisMember = new CtsMemberRelation();
        thisMember.setAccount(member.getAccount());
        for (CtsMemberRelation memberRelation : memberList) {
            likeMap.put(memberRelation.getAccount(), memberRelation);
        }

        session.getBasicRemote().sendText(Result.r("memberMap", Result.success,likeMap));
    }

    public void memberMessage(JSONObject parse, Session session) {
    }

    public void addMember(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        JSONObject memberObject = parse.getJSONObject("data");
        CtsMemberRelation memberRelation = new CtsMemberRelation();
        memberRelation.setMemberId(member.getId());
        memberRelation.setId(IdUtil.objectId());
        memberRelation.setCreateTime(DateUtil.date());
        memberRelation.setUsername(memberObject.getString("username"));
        memberRelation.setNickname(memberObject.getString("username"));
        memberRelation.setAccount(memberObject.getString("account"));
        memberRelation.setState(0);
        memberRelationRepository.save(memberRelation);
    }

    public void createGroup(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        JSONObject groupObject = parse.getJSONObject("data");

        CtsGroup group = new CtsGroup();
        group.setId(IdUtil.objectId());
        group.setCreateTime(DateUtil.date());
        group.setName(groupObject.getString("name"));
        group.setCreateMemberId(member.getId());
        groupRepository.save(group);

        CtsGroupRelation groupRelation = new CtsGroupRelation();
        groupRelation.setId(IdUtil.objectId());
        groupRelation.setGroupAccount(group.getAccount());
        groupRelation.setMemberAccount(member.getAccount());
        groupRelation.setMemberNickname(member.getUsername());
        groupRelation.setMemberRole("1");
        groupRelation.setCreateTime(DateUtil.date());
        groupRelation.setAlias(member.getUsername());
        groupRelation.setNickname(member.getUsername());
        groupRelationRepository.save(groupRelation);
    }

    public void saveMember(JSONObject parse, Session session) {

    }

    public void saveMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        CtsMemberMessage memberMessage = new CtsMemberMessage();
        try {
            memberMessage.setId(IdUtil.objectId());
            memberMessage.setServiceId(data.getString("serviceId"));
            memberMessage.setSendAccount(member.getAccount());
            memberMessage.setAcceptAccount(data.getString("withAccount"));
            memberMessage.setCreateTime(DateUtil.date());
            memberMessage.setContent(data.getString("content"));
            memberMessage.setAccount(member.getAccount());
            memberMessage.setWithAccount(data.getString("withAccount"));
            memberMessageRepository.save(memberMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    public void loadMemberMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        String beAccount = member.getAccount();
        String withAccount = data.getString("account");
        List<CtsMemberMessage> messageList = memberMessageRepository.findByAccountAndWithAccount(beAccount, withAccount);
        Map<String, Object> result = new HashMap<>();
        result.put("account", data.getString("account"));
        result.put("data", messageList);
        session.getAsyncRemote().sendText(Result.r("loadMemberMessage", Result.success,result));
    }

    public void join(JSONObject params, Session session) {
        
    }

    public void loadMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberId(member.getId());
        JSONArray jsonArray = new JSONArray();
        memberList.forEach(memberRelation -> {
            String account = memberRelation.getAccount();
            List<CtsMemberMessage> memberMessage = memberMessageRepository.findByAccountAndWithAccount(member.getAccount(), account);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", account);
            jsonObject.put("data", memberMessage);
            jsonArray.add(jsonObject);
        });
        session.getAsyncRemote().sendText(Result.r("loadMessage", Result.success,jsonArray));
    }

    public void deleteMember(JSONObject data, Session session) {
        String account = data.getString("account");
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        memberRelationRepository.deleteByMemberIdAndAccount(member.getId(), account);
        
    }

}
