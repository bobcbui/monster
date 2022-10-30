package net.ttcxy.chat.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSONObject;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class FastController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RelationGroupRepository relationGroupRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    MemberRepository memberRepository;


    @PostMapping("/authenticate")
    public void authorize(@RequestBody JSONObject loginParam) {
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        CtsMember member = memberRepository.findByUsername(username);
        if(BCrypt.checkpw(password, member.getPassword())){
            request.getSession().setAttribute("member", member);
        }
        throw new ApiException("密码或用户名不正确！");
    }

    @PostMapping("register")
    public CtsMember register(@RequestBody JSONObject loginParam){
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        password = BCrypt.hashpw(password);
        CtsMember member = new CtsMember();
        member.setUsername(username);
        member.setCreateTime(new Date());
        member.setPassword(password);
        return memberRepository.save(member);
    }

    @PostMapping("group/create")
    public CtsGroup addGroup(@RequestBody JSONObject object){
        String nickname = object.getString("nickname");
        CtsMember member = getMember();
        CtsGroup group = new CtsGroup();
        group.setCreateMemberId(member.getId());
        group.setNickname(nickname);
        group.setCreateTime(DateUtil.date());

        group = groupRepository.save(group);

        CtsRelationGroup relationGroup = new CtsRelationGroup();
        relationGroup.setMemberId(member.getId());
        relationGroup.setBeGroupId(group.getId());
        relationGroup.setPass(true);
        relationGroupRepository.save(relationGroup);

        return group;
    }

    public CtsMember getMember(){
        return (CtsMember)request.getSession().getAttribute("member");
    }
}
