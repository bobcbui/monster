package net.ttcxy.chat.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.websocket.Session;
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

    public void groupListHandler(JSONObject data, Session session) throws IOException{
        CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
        List<CtsGroup> groupList = groupRepository.findByMemberId(member.getId());
        Map<String,Object> result = new HashMap<>();
        result.put("type", "groupList");
        result.put("data", JSON.toJSONString(groupList));
        session.getBasicRemote().sendText(JSON.toJSONString(result));
    }

    public void memberListHandler(JSONObject data, Session session) throws IOException{
        CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
        List<CtsMemberRelation> memberList = memberRelationRepository.findByMemberId(member.getId());
        Map<String,Object> result = new HashMap<>();
        result.put("type", "memberList");
        result.put("data", JSON.toJSONString(memberList));
        session.getBasicRemote().sendText(JSON.toJSONString(result));
    }

    public void memberMessageHandler(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
        List<CtsMemberMessage> memberMessageList = memberMessageRepository.findMemberMessageByMemberId(member.getId());
    }

    public void addMemberHandler(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
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

    public void createGroupHandler(JSONObject parse, Session session) {
        CtsMember member = (CtsMember) session.getUserProperties().get("memberData");
        JSONObject groupObject = parse.getJSONObject("data");
        CtsGroup group = new CtsGroup();
        group.setId(IdUtil.objectId());
        group.setCreateTime(DateUtil.date());
        group.setName(groupObject.getString("name"));
        group.setCreateMemberId(member.getId());
        groupRepository.save(group);
        CtsGroupRelation groupRelation = new CtsGroupRelation();
        groupRelation.setId(IdUtil.objectId());
        groupRelation.setGroupId(group.getId());
        groupRelation.setMemberId(member.getId());
        groupRelation.setCreateTime(DateUtil.date());
        groupRelation.setMemberRole("1");
        groupRelation.setMemberNickname(member.getUsername());
        groupRelation.setMemberAccount(member.getAccount());
        groupRelationRepository.save(groupRelation);
    }

    public void saveMemberHandler(JSONObject parse, Session session) {
    }

}
