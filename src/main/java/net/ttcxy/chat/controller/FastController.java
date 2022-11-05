package net.ttcxy.chat.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.code.api.ApiException;
import net.ttcxy.chat.entity.model.CtsGroup;
import net.ttcxy.chat.entity.model.CtsMember;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.MemberRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;

@RestController
@RequestMapping
public class FastController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RelationGroupRepository relationGroupRepository;

    @Autowired
    HttpServletRequest request;

    @PostMapping("/authenticate")
    public String authorize(@RequestBody JSONObject loginParam) {
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        CtsMember member = memberRepository.findByUsername(username);

        if(BCrypt.checkpw(password, member.getPassword())){
            String token = UUID.randomUUID().toString();
            ApplicationData.tokenMemberMap.put(token, member);
            return token;
        }
        throw new ApiException("密码或用户名不正确！");
    }

    @PostMapping("register")
    public CtsMember register(@RequestBody JSONObject loginParam){
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        CtsMember member = new CtsMember();
        member.setUsername(username);
        member.setCreateTime(new Date());
        member.setPassword(password);
        return memberRepository.save(member);
    }

    @PostMapping("token")
    public ResponseEntity<String> createToken(){
        String token = UUID.randomUUID().toString();
        if(getMember() != null){
            ApplicationData.tokenSocketMap.put(token, getMember());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).build();
        
    }

    @GetMapping("username/{username}/token/{token}")
    public ResponseEntity<?> checkToken(@PathVariable("username")String username, @PathVariable("token")String token){
        CtsMember ctsMember = ApplicationData.tokenSocketMap.get(token);
        if(ctsMember != null && ctsMember.getUsername().equals(username)){
            return ResponseEntity.ok(ctsMember);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("group/create")
    public CtsGroup createGroup(@RequestBody JSONObject object){
        String nickname = object.getString("nickname");
        CtsMember member = getMember();
        CtsGroup group = new CtsGroup();
        group.setCreateMemberId(member.getId());
        group.setNickname(nickname);
        group.setCreateTime(new Date());
        groupRepository.save(group);

        CtsRelationGroup relationGroup = new CtsRelationGroup();
        relationGroup.setMemberUrl("ws://localhost:9090/"+member.getUsername());
        relationGroup.setBeGroupUrl("ws://localhost:9090/group/"+group.getId());
        relationGroup.setPass(true);
        relationGroupRepository.save(relationGroup);

        return group;
    }

    public CtsMember getMember(){
        return ApplicationData.tokenMemberMap.get(request.getHeader("token"));
    }
}
