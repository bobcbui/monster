package com.ooqn.chat.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsGroup;
import com.ooqn.chat.entity.CtsGroupRelation;
import com.ooqn.chat.entity.CtsMember;
import com.ooqn.chat.entity.CtsMemberMessage;
import com.ooqn.chat.entity.CtsMemberRelation;
import com.ooqn.chat.entity.CtsVerify;
import com.ooqn.chat.repository.GroupRelationRepository;
import com.ooqn.chat.repository.GroupRepository;
import com.ooqn.chat.repository.MemberMessageRepository;
import com.ooqn.chat.repository.MemberRelationRepository;
import com.ooqn.chat.repository.VerifyRepository;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;

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
    
    @Autowired
    private VerifyRepository verifyRepository;

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
        session.getBasicRemote().sendText(Result.r(data.getString("transactionId"), "groupList", Result.success, jsonArray));
    }

    public void memberMap(JSONObject data, Session session) throws IOException {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberIdAndState(member.getId(), 1);
        // memberList to Map 
        Map<String,CtsMemberRelation> likeMap = new LinkedHashMap<>();
        CtsMemberRelation thisMember = new CtsMemberRelation();
        thisMember.setAccount(member.getAccount());
        for (CtsMemberRelation memberRelation : memberList) {
            likeMap.put(memberRelation.getAccount(), memberRelation);
        }

        session.getBasicRemote().sendText(Result.r(data.getString("transactionId"), "memberMap", Result.success,likeMap));
    }

    public void memberMessage(JSONObject parse, Session session) {
    }

    public void joinMember(JSONObject parse, Session session) {
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

        JSONObject dataJson = new JSONObject();
        dataJson.put("account", memberObject.getString("account"));
        dataJson.put("context", parse.getString("context"));
        dataJson.put("username", memberObject.getString("username"));

        CtsVerify verify = new CtsVerify();
        verify.setId(IdUtil.objectId());
        verify.setMemberId(member.getId());
        verify.setCreateTime(DateUtil.date());
        verify.setUpdateTime(DateUtil.date());
        verify.setState(3);
        verify.setType("1");
        verify.setData(dataJson.toJSONString());

        verifyRepository.save(verify);
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

            // 保存成功
            session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "saveMessage", Result.success));
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
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "loadMemberMessage", Result.success,result));
    }

    public void loadMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberIdAndState(member.getId(), 1);
        JSONArray jsonArray = new JSONArray();
        memberList.forEach(memberRelation -> {
            String account = memberRelation.getAccount();
            List<CtsMemberMessage> memberMessage = memberMessageRepository.findByAccountAndWithAccount(member.getAccount(), account);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", account);
            jsonObject.put("data", memberMessage);
            jsonArray.add(jsonObject);
        });
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "loadMessage", Result.success,jsonArray));
    }

    public void deleteMember(JSONObject data, Session session) {
        String account = data.getString("account");
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        memberRelationRepository.deleteByMemberIdAndAccount(member.getId(), account);
        
    }


    public void loadVerify(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsVerify> verifyList = verifyRepository.findByMemberId(member.getId());
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "loadVerify", Result.success,verifyList));
    }

    public void deleteVerify(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        verifyRepository.deleteByMemberIdAndId(member.getId(), data.getString("verifyId"));
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "deleteVerify", Result.success));

    }

    public void agreeVerify(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        Optional<CtsVerify> verify = verifyRepository.findById(data.getString("verifyId"));
        if(!verify.get().getMemberId().equals(member.getId())) {
            return;
        }
        String cString = verify.get().getData();
        JSONObject jsonObject = JSONObject.parseObject(cString);
        verify.get().setState(1);
        verify.get().setUpdateTime(DateUtil.date());
        verifyRepository.save(verify.get());
        CtsMemberRelation memberRelation = memberRelationRepository.findByMemberIdAndAccount(member.getId(), jsonObject.getString("account"));
        memberRelation.setState(1);
        memberRelation.setUpdateTime(DateUtil.date());
        memberRelationRepository.save(memberRelation);

        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "deleteVerify", Result.success));
    }

    public void rejectVerify(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        Optional<CtsVerify> verify = verifyRepository.findById(data.getString("verifyId"));
        if(!verify.get().getMemberId().equals(member.getId())) {
            return;
        }
        String cString = verify.get().getData();
        JSONObject jsonObject = JSONObject.parseObject(cString);
        verify.get().setState(2);
        verify.get().setUpdateTime(DateUtil.date());
        verifyRepository.save(verify.get());
        CtsMemberRelation memberRelation = memberRelationRepository.findByMemberIdAndAccount(member.getId(), jsonObject.getString("account"));
        memberRelation.setState(1);
        memberRelation.setUpdateTime(DateUtil.date());
        memberRelationRepository.save(memberRelation);

        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "deleteVerify", Result.success));
    }

    public void updateMemberReadTime(JSONObject data, Session session) {
        // memberAccount
         String account = data.getString("account");
         CtsMember member = (CtsMember) session.getUserProperties().get("member");
         long time = DateUtil.date().getTime();
         memberRelationRepository.updateReadTime(account, member.getId(), DateUtil.date());
         session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "updateGroupReadTime", Result.success, time));

    }

    public void updateGroupReadTime(JSONObject data, Session session) {
        // groupAccount
        String account = data.getString("account");
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        long time = DateUtil.date().getTime();
        groupRelationRepository.updateReadTime(account, member.getId(), DateUtil.date());
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "updateGroupReadTime", Result.success, time));
    }

    public void recommendGroup(JSONObject data, Session session) {
        List<CtsGroup> groupList = groupRepository.findLimit10();
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "recommendGroup", Result.success, groupList));
    }


}
