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

    public void messages(JSONObject param, Session session) {
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");

        String memberRole = memberRole(sendMember.getString("account"), acceptGroup.getAccount());

        if(memberRole == null){
            session.getAsyncRemote().sendText(Result.r(param, Result.error,"您不是该群成员了！"));
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
        session.getAsyncRemote().sendText(Result.success(param, resultObject));
    }

    public void members(JSONObject param, Session session) {

    }

    public void info(JSONObject param, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");

        JSONObject resultObj = JSONObject.parseObject(JSONObject.toJSONString(acceptGroup));
        String account = sendMember.getString("account"); 
        String memberRole = memberRole(account, acceptGroup.getAccount());

        CtsGroupRelation groupRelation = groupRelationRepository.findByMemberAccountAndGroupAccount(sendMember.getString("account"), acceptGroup.getAccount());
        resultObj.put("readTime", groupRelation.getReadTime());
        
        if(memberRole != null){
            JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(acceptGroup));
            parseObject.put("memberRole", memberRole);
        }
        
        session.getAsyncRemote().sendText(Result.success(param, resultObj));
    }

    public void notion(JSONObject parse, Session session) {

    }

    public void message(JSONObject param, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");

        CtsGroupMessage groupMessage = new CtsGroupMessage();
        groupMessage.setId(IdUtil.objectId());
        groupMessage.setAcceptGroupId(acceptGroup.getId());
        groupMessage.setAccount(sendMember.getString("account"));
        groupMessage.setContent(param.getString("content"));
        groupMessage.setCreateTime(DateUtil.date());
        groupMessageRepository.save(groupMessage);

        for (Entry<String, List<Session>> entrySet : GroupSocket.groupSession.entrySet()) {
            if (entrySet.getKey().equals(acceptGroup.getAccount())) {
                List<Session> list = entrySet.getValue();
                for (Session value : list) {
                    if (value.isOpen()) {
                        JSONObject message = new JSONObject();
                        message.put("content", param.getString("content"));
                        message.put("createTime", DateUtil.date());
                        message.put("sendAccount", sendMember.getString("account"));
                        message.put("sendNickname", sendMember.getString("username"));
                        message.put("withGroupAccount", acceptGroup.getAccount());
                        try {
                            value.getBasicRemote().sendText(Result.success(param, message));
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

    public void join(JSONObject param, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");

        CtsGroupRelation groupRelation = new CtsGroupRelation();
        groupRelation.setId(IdUtil.objectId());
        groupRelation.setGroupAccount(acceptGroup.getAccount());
        groupRelation.setMemberAccount(sendMember.getString("account"));
        groupRelation.setMemberNickname(sendMember.getString("username"));
        groupRelation.setMemberRole("3");
        groupRelation.setCreateTime(DateUtil.date());
        groupRelation.setAlias(sendMember.getString("username"));
        groupRelation.setNickname(sendMember.getString("username"));
        CtsGroupRelation save = groupRelationRepository.save(groupRelation);
        if(save == null){
            return;
        }
        session.getAsyncRemote().sendText(Result.success(param, acceptGroup));
        // 通知群内其他成员
        for (Entry<String, List<Session>> entrySet : GroupSocket.groupSession.entrySet()) {
            if (entrySet.getKey().equals(acceptGroup.getAccount())) {
                List<Session> list = entrySet.getValue();
                for (Session value : list) {
                    if (value.isOpen()) {
                        JSONObject message = new JSONObject();
                        message.put("content", sendMember.getString("username") + "加入了群聊");
                        message.put("createTime", DateUtil.date());
                        message.put("sendAccount", "system");
                        message.put("sendNickname", "system");
                        message.put("withGroupAccount", acceptGroup.getAccount());
                        try {
                            value.getBasicRemote().sendText(Result.success(param, message));
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

    public void quit(JSONObject param, Session session) {
        CtsGroup acceptGroup = (CtsGroup) session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject) session.getUserProperties().get("sendMember");
        CtsGroupRelation groupRelation = groupRelationRepository.findByMemberAccountAndGroupAccount(sendMember.getString("account"), acceptGroup.getAccount());
        groupRelationRepository.delete(groupRelation);
        session.getAsyncRemote().sendText(Result.success(param,groupRelation));
    }

    private String memberRole(String account, String groupAccount) {
        CtsGroupRelation groupRelation = groupRelationRepository.findByMemberAccountAndGroupAccount(account, groupAccount);
        if (groupRelation != null) {
            return groupRelation.getMemberRole();
        }
        return null;
    }
}
