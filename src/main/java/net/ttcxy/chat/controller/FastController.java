package net.ttcxy.chat.controller;

import java.util.Date;
import java.util.Optional;
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
import net.ttcxy.chat.entity.model.CtsUser;
import net.ttcxy.chat.entity.model.CtsRelationGroup;
import net.ttcxy.chat.repository.GroupRepository;
import net.ttcxy.chat.repository.UserRepository;
import net.ttcxy.chat.repository.RelationGroupRepository;

@RestController
@RequestMapping
public class FastController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RelationGroupRepository relationGroupRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("user")
    public CtsUser user(){
        return getUser();
    }

    @PostMapping("authenticate")
    public String authorize(@RequestBody JSONObject loginParam) {
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        Optional<CtsUser> userOpt = userRepository.findById(username);

        if(BCrypt.checkpw(password, userOpt.orElseThrow().getPassword())){
            String token = UUID.randomUUID().toString();
            ApplicationData.tokenUserMap.put(token, userOpt.orElseThrow());
            return token;
        }
        throw new ApiException("密码或用户名不正确！");
    }

    @PostMapping("register")
    public CtsUser register(@RequestBody JSONObject loginParam){
        String username = loginParam.getString("username");
        String password = loginParam.getString("password");
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        CtsUser user = new CtsUser();
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setPassword(password);
        return userRepository.save(user);
    }

    @PostMapping("token")
    public ResponseEntity<String> createToken(){
        String token = UUID.randomUUID().toString();
        if(getUser() != null){
            ApplicationData.tokenSocketMap.put(token, getUser());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).build();
        
    }

    @GetMapping("username/{username}/token/{token}")
    public ResponseEntity<?> checkToken(@PathVariable("username")String username, @PathVariable("token")String token){
        CtsUser user = ApplicationData.tokenSocketMap.get(token);
        if(user != null && user.getUsername().equals(username)){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("group/create")
    public CtsGroup createGroup(@RequestBody JSONObject object){
        String name = object.getString("name");
        CtsUser user = getUser();
        CtsGroup group = new CtsGroup();
        group.setGroupName(name);
        group.setCreateUsername(user.getUsername());
        group.setNickname(name);
        group.setCreateTime(new Date());
        groupRepository.save(group);

        CtsRelationGroup relationGroup = new CtsRelationGroup();
        relationGroup.setWs("ws://localhost:9090/"+user.getUsername());
        relationGroup.setGroupName(name);
        relationGroup.setPass(true);
        relationGroupRepository.save(relationGroup);

        return group;
    }

    public CtsUser getUser(){
        return ApplicationData.tokenUserMap.get(request.getHeader("token"));
    }
}
