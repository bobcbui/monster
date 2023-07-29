package com.ooqn.chat.service;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ooqn.chat.code.Result;
import com.ooqn.chat.entity.CtsGroup;
import com.ooqn.chat.entity.CtsGroupMessage;
import com.ooqn.chat.entity.CtsGroupRelation;
import com.ooqn.chat.repository.GroupMessageRepository;
import com.ooqn.chat.repository.GroupRelationRepository;
import com.ooqn.chat.socket.GroupSocket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;

@Service
public class GroupSocketService {

    @Autowired
    private GroupRelationRepository groupRelationRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    public void messages(JSONObject data, Session session) {
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");

        String memberRole = memberRole(sendMember.getString("account"), acceptGroup.getAccount());

        if(memberRole == null){
            session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "messages", Result.error, "您不是该群成员了！"));
            return;
        }

        List<CtsGroupMessage> groupMessageList = groupMessageRepository.findByAcceptGroupId(acceptGroup.getId());
        JSONArray resultObject = new JSONArray();
        groupMessageList.forEach(groupMessage -> {
            JSONObject message = new JSONObject();
            CtsGroupRelation relation = groupRelationRepository
                    .findByMemberAccountAndGroupAccount(groupMessage.getAccount(), acceptGroup.getAccount());
            message.put("type", "message");
            message.put("content", groupMessage.getContent());
            message.put("sendAccount", relation.getMemberAccount());
            message.put("sendNickname", relation.getMemberNickname());
            message.put("createTime", groupMessage.getCreateTime());
            message.put("withGroupAccount", acceptGroup.getAccount());
            resultObject.add(0, message);
        });
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "messages", Result.success, resultObject));
    }

    public void members(JSONObject data, Session session) {

    }

    public void info(JSONObject data, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject resultObj = JSONObject.parseObject(JSONObject.toJSONString(acceptGroup));
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        String account = sendMember.getString("account"); 
        String memberRole = memberRole(account, acceptGroup.getAccount());
        
        if(memberRole != null){
            JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(acceptGroup));
            parseObject.put("memberRole", memberRole);
        }
        
        session.getAsyncRemote().sendText(Result.r(data.getString("transactionId"), "info", Result.success, resultObj));
    }

    public void notion(JSONObject data, Session session) {

    }

    public void message(JSONObject data, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");

        CtsGroupMessage groupMessage = new CtsGroupMessage();
        groupMessage.setId(IdUtil.objectId());
        groupMessage.setAcceptGroupId(acceptGroup.getId());
        groupMessage.setAccount(sendMember.getString("account"));
        groupMessage.setContent(data.getString("content"));
        groupMessage.setCreateTime(DateUtil.date());
        groupMessageRepository.save(groupMessage);

        for (Entry<String, List<Session>> entrySet : GroupSocket.groupSession.entrySet()) {
            if (entrySet.getKey().equals(acceptGroup.getAccount())) {
                List<Session> list = entrySet.getValue();
                for (Session value : list) {
                    if (value.isOpen()) {
                        JSONObject message = new JSONObject();
                        message.put("content", data.getString("content"));
                        message.put("createTime", DateUtil.date());
                        message.put("sendAccount", sendMember.getString("account"));
                        message.put("sendNickname", sendMember.getString("username"));
                        message.put("withGroupAccount", acceptGroup.getAccount());
                        try {
                            value.getBasicRemote().sendText(Result.r(data.getString("transactionId"), "message", Result.success, message));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        list.remove(value);
                    }
                }

            }
        }
    }

    public void join(JSONObject jsonObject, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        JSONObject data = jsonObject.getJSONObject("data");

        CtsGroupRelation groupRelation = new CtsGroupRelation();
        groupRelation.setId(IdUtil.objectId());
        groupRelation.setGroupAccount(acceptGroup.getAccount());
        groupRelation.setMemberAccount(data.getString("memberAccount"));
        groupRelation.setMemberNickname(sendMember.getString("username"));
        groupRelation.setMemberRole("3");
        groupRelation.setCreateTime(DateUtil.date());
        groupRelation.setAlias(acceptGroup.getName());
        groupRelation.setNickname(acceptGroup.getName());
        groupRelationRepository.save(groupRelation);
    }

    private String memberRole(String account, String groupAccount) {
        CtsGroupRelation groupRelation = groupRelationRepository.findByMemberAccountAndGroupAccount(account, groupAccount);
        if (groupRelation != null) {
            return groupRelation.getMemberRole();
        }
        return null;
    }

}
