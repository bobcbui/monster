package net.ttcxy.chat.service;

import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
import net.ttcxy.chat.code.ResultMap;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.entity.CtsGroupMessage;
import net.ttcxy.chat.entity.CtsGroupRelation;
import net.ttcxy.chat.repository.GroupMessageRepository;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.socket.GroupSocket;

@Service
public class GroupSocketService {

    @Autowired
    private GroupRelationRepository groupRelationRepository;

    /**
     * 加载群消息
     */
    public void groupMessage(JSONObject data, Session session){
        CtsGroup acceptGroup = (CtsGroup)session.getUserProperties().get("acceptGroup");
        List<CtsGroupMessage> groupMessageList = groupMessageRepository.findByAcceptGroupId(acceptGroup.getId());
        JSONArray resultObject = new JSONArray();
        groupMessageList.forEach(groupMessage -> {
            JSONObject message = new JSONObject();
            CtsGroupRelation relation = groupRelationRepository.findByMemberAccountAndGroupId(groupMessage.getAccount(), acceptGroup.getId());
            message.put("type", "message");
            message.put("content", groupMessage.getContent());
            message.put("sendAccount", relation.getMemberAccount());
            message.put("sendNickname", relation.getMemberNickname());
            message.put("createTime", DateUtil.format(groupMessage.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            resultObject.add(message);
        });
        session.getAsyncRemote().sendText(ResultMap.result("groupMessage", resultObject));
    }

    /**
     * 发送消息
     */
    public void sendMessage(JSONObject data, Session session){

    }

    /**
     * 加载群成员列表
     */
    public void groupMember(JSONObject data, Session session){

    }

    /**
     * 加载群信息
     */
    public void groupInfo(JSONObject data, Session session){

    }

    /**
     * 加载通知
     */
    public void notion(JSONObject data, Session session){

    }

    @Autowired
    GroupMessageRepository groupMessageRepository;
    private Object findByAccountAndGroupId;

    /**
     * 获取消息
     */
    public void message(JSONObject jsonObject, Session session) {
        CtsGroup acceptGroup = (CtsGroup)session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject)session.getUserProperties().get("sendMember");


        CtsGroupMessage groupMessage = new CtsGroupMessage();
        groupMessage.setId(IdUtil.objectId());
        groupMessage.setAcceptGroupId(acceptGroup.getId());
        groupMessage.setAccount(sendMember.getString("account"));
        groupMessage.setContent(jsonObject.getString("content"));
        groupMessage.setCreateTime(DateUtil.date());

        groupMessageRepository.save(groupMessage);

        for (Entry<String,List<Session>> entrySet : GroupSocket.groupSession.entrySet()) {
            if(entrySet.getKey().equals(acceptGroup.getAccount())){
                List<Session> list = entrySet.getValue();
               for (Session value : list) {
                    if(value.isOpen()){
                        JSONObject message = new JSONObject();
                        
                        message.put("type", "message");
                        message.put("content", jsonObject.getString("content"));
                        message.put("createTime", DateUtil.date());
                        message.put("sendAccount", sendMember.getString("account"));
                        message.put("sendNickname", sendMember.getString("username"));
                        try {
                            value.getBasicRemote().sendText(message.toJSONString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        list.remove(value);
                    }
               }
                
            }
        }
    }

    public void joinGroup(JSONObject jsonObject, Session session) {
        CtsGroup acceptGroup = (CtsGroup)session.getUserProperties().get("acceptGroup");
        JSONObject sendMember = (JSONObject)session.getUserProperties().get("sendMember");

        JSONObject data = jsonObject.getJSONObject("data");

        CtsGroupRelation groupRelation = new CtsGroupRelation();
        groupRelation.setId(IdUtil.objectId());
        groupRelation.setGroupId(acceptGroup.getId());
        groupRelation.setMemberAccount(data.getString("memberAccount"));
        groupRelation.setMemberNickname(sendMember.getString("username"));
        groupRelation.setMemberRole("3");
        groupRelation.setCreateTime(DateUtil.date());
        groupRelationRepository.save(groupRelation);

    }
    
}
