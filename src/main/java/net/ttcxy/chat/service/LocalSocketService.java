package net.ttcxy.chat.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
import net.ttcxy.chat.code.ResultMap;
import net.ttcxy.chat.entity.CtsGroup;
import net.ttcxy.chat.entity.CtsGroupRelation;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.entity.CtsMemberMessage;
import net.ttcxy.chat.entity.CtsMemberRelation;
import net.ttcxy.chat.repository.GroupRelationRepository;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberMessageRepository;
import net.ttcxy.chat.repository.MemberRelationRepository;

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
        List<CtsGroupRelation> groupList = groupRelationRepository.findByMemberAccount(member.getAccount());
        session.getBasicRemote().sendText(ResultMap.result("groupList", groupList));
    }

    /**
     * 好友列表指令处理器
     */
    public void memberList(JSONObject data, Session session) throws IOException {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberId(member.getId());
        session.getBasicRemote().sendText(ResultMap.result("memberList", memberList));
    }

    public void memberMessage(JSONObject parse, Session session) {
    }

    /**
     * 添加好友指令处理器
     */
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

    /**
     * 创建群指令处理器
     */
    public void createGroup(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        JSONObject groupObject = parse.getJSONObject("data");
        // 创建群
        CtsGroup group = new CtsGroup();
        group.setId(IdUtil.objectId());
        group.setCreateTime(DateUtil.date());
        group.setName(groupObject.getString("name"));
        group.setCreateMemberId(member.getId());
        groupRepository.save(group);

        // 将群主加入群
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

    /**
     * 发送消息处理器
     */
    public void saveMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        CtsMemberMessage memberMessage = new CtsMemberMessage();
        memberMessage.setId(IdUtil.objectId());
        memberMessage.setOrderId(data.getString("orderId"));
        memberMessage.setSendAccount(member.getAccount());
        memberMessage.setAcceptAccount(data.getString("account"));
        memberMessage.setCreateTime(DateUtil.date());
        memberMessage.setContent(data.getString("content"));
        memberMessage.setAccount(member.getAccount());
        memberMessage.setWithAccount(data.getString("account"));
        memberMessageRepository.save(memberMessage);
    }

    /**
     * 加载好友消息处理器
     */
    public void loadMemberMessage(JSONObject data, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("member");
        String beAccount = member.getAccount();
        String withAccount = data.getString("account");
        List<CtsMemberMessage> messageList = memberMessageRepository
                .findByAccountAndWithAccount(beAccount, withAccount);
        Map<String, Object> result = new HashMap<>();
        result.put("account", data.getString("account"));
        result.put("data", messageList);
        session.getAsyncRemote().sendText(ResultMap.result("loadMemberMessage", result));
    }

    public void joinGroup(JSONObject params, Session session) {
        // String groupAccount = params.getString("groupAccount");
        // CtsMember member = (CtsMember) session.getUserProperties().get("member");

        // JSONObject data = params.getJSONObject("data");

        // CtsGroupRelation groupRelation = new CtsGroupRelation();
        // groupRelation.setId(IdUtil.objectId());
        // groupRelation.setGroupAccount(groupAccount);
        // groupRelation.setMemberAccount(data.getString("memberAccount"));
        // groupRelation.setMemberNickname(member.getUsername());
        // groupRelation.setMemberRole("3");
        // groupRelation.setCreateTime(DateUtil.date());
        // groupRelationRepository.save(groupRelation);
    }

}
