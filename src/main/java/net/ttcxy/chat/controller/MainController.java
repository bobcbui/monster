package net.ttcxy.chat.controller;

import java.util.Date;
import java.util.UUID;

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

import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.ttcxy.chat.code.ApplicationData;
import net.ttcxy.chat.entity.CtsMember;
import net.ttcxy.chat.repository.MemberRepository;

@RestController
@RequestMapping
public class MainController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("member")
    public ResponseEntity<CtsMember> member(){
        CtsMember member = getMember();
        if(member == null){
            return ResponseEntity.status(401).body(member);
        }
        return ResponseEntity.status(200).build();
    }

    /**
     * 用户登录
     * @param login
     * @return
     */
    @PostMapping("authenticate")
    public String authorize(@RequestBody JSONObject login) {
        String username = login.getString("username");
        String password = login.getString("password");
        CtsMember member = memberRepository.findByUsername(username);

        if(BCrypt.checkpw(password, member.getPassword())){
            String token = UUID.randomUUID().toString();
            ApplicationData.tokenMemberMap.put(token, member);
            return token;
        }
        throw new RuntimeException("密码或用户名不正确！");
    }
    
    /**
     * 用户登录
     * @param register
     * @return
     */
    @PostMapping("register")
    public CtsMember register(@RequestBody JSONObject register){
        String username = register.getString("username");
        String password = register.getString("password");
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        CtsMember member = new CtsMember();
        member.setId(IdUtil.objectId());
        member.setUsername(username);
        member.setCreateTime(new Date());
        member.setPassword(password);
        return memberRepository.save(member);
    }

    /*
     * 获取用户信息
     */
    @GetMapping("authenticate/info")
    public ResponseEntity<CtsMember> checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        CtsMember member = ApplicationData.tokenMemberMap.get(token);
        if(member == null){
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(member);
    }

    /**
     * OneToken 用于添加好友，添加群时给其他服务器验证Token是否有效的一个一次性Token
     * @return
     */
    @GetMapping("one-token")
    public ResponseEntity<String> createToken(){
        String token = IdUtil.randomUUID();
        if(getMember() != null){
            ApplicationData.tokenSocketMap.put(token, getMember());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).build();
        
    }

    /**
     * 验证 one-token
     * @param token
     * @return
     */
    @GetMapping("/check/{token}")
    public CtsMember checkOneToken(@PathVariable("token") String token){
        CtsMember ctsMember = ApplicationData.tokenSocketMap.get(token);
        ApplicationData.tokenSocketMap.remove(token);
        return ctsMember;
    }

    @GetMapping("/logout")
    public void logout(){
        ApplicationData.tokenMemberMap.remove(request.getHeader("token"));
    }

    /**
     * 获取登录的用户
     * @return
     */
    private CtsMember getMember(){
        return ApplicationData.tokenMemberMap.get(request.getHeader("token"));
    }
}
